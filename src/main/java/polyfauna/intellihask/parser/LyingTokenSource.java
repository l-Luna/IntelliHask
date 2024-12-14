package polyfauna.intellihask.parser;

import com.intellij.lang.PsiBuilder;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import polyfauna.intellihask.psi.Tokens;
import org.antlr.intellij.adaptor.lexer.PSITokenSource;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayDeque;
import java.util.Deque;

// inserts SEMI, VOCURLY, and VCCURLY tokens for parsing, to be removed later
public class LyingTokenSource extends PSITokenSource{
	
	protected static class Block{
		int indent;
		int parens;
		boolean isLetBlock;
		
		public Block(int indent, boolean isLetBlock){
			this.indent = indent;
			this.isLetBlock = isLetBlock;
		}
	}
	
	protected Deque<Token> preempted = new ArrayDeque<>();
	protected Deque<Block> blocks = new ArrayDeque<>();
	protected boolean wasBlockKw = false, wasLetKw = false;
	// we don't *actually* have access to line numbers or indexes, but we see newlines, so like same difference
	protected boolean seenNewline = false;
	protected int lastLineOffset = 0;
	
	public LyingTokenSource(PsiBuilder builder){
		super(builder);
		// we also don't *actually* see newlines; instead we reconstruct them from the skipping callback
		builder.setWhitespaceSkippedCallback((type, start, end) -> {
			if(type == Tokens.getFor(HaskellLexer.NEWLINE)){
				seenNewline = true;
				lastLineOffset = start;
			}
		});
	}
	
	// on getting a new token,
	//   if it's the first token of this line,
	//     if the previous token was a keyword, this indent defines the start of a block
	//       -> produce a VOCURLY, push a new indent to the stack, and schedule *this* token next
	//       -> TODO: if the indentation is smaller than the previous block indent, error; could just not create VOCURLY, or handle later?
	//     if this has the same or greater indent than the current block, then this is a statement
	//       -> produce a SEMI, schedule this token next
	//     if this has less indent than the current block, this ends the block
	//       -> produce a VCCURLY, pop the indent from the stack, and schedule this token next
	//       -> apply this repeatedly for each closed block
	//   if it's a newline, consider its indent to be 0 always
	//   if there's no current block, consider the current block indent to be 0
	
	public Token nextToken(){
		if(!preempted.isEmpty())
			return preempted.pop();
		
		Token next = super.nextToken(); // possibly triggers the whitespace callback
		preempted.push(next);
		int indent = next.getStartIndex() - lastLineOffset - 1;
		// start a new block after any block kw not followed by {, even if there's no newline
		boolean noExtraSemi = false;
		if(wasBlockKw && next.getType() != HaskellLexer.OCURLY){
			pushBlock(indent);
			preempted.push(createTok(HaskellLexer.VOCURLY, "VOCURLY", next.getStopIndex()));
			// don't start every block with an unnecessary semi
			noExtraSemi = true;
		}
		
		// adjust parens for this block
		if(next.getType() == HaskellLexer.OpenRoundBracket)
			afterParen(1);
		if(next.getType() == HaskellLexer.CloseRoundBracket)
			afterParen(-1);
		// if the current block has dangling parenthesis, we know we should finish early
		// e.g. `(case h of \{ [] -> [] \; \})`
		if(isDanglingParen()){
			popBlock();
			preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY-P", next.getStopIndex()));
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-P", next.getStopIndex()));
			// if the current block was implicitly triggered by a `let` keyword, the `in` keyword can also end it early
			// e.g. `let \{ p = 1 \; \} in p`
			// but we don't want normal semis to be inserted in the multiline case,
			// i.e. `let \n \{ p = 1 \n \; \} \; in p` would be unwanted
		}else if(isLetBlock() && next.getType() == HaskellLexer.IN){
			popBlock();
			preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY-L", next.getStopIndex()));
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-L", next.getStopIndex()));
			noExtraSemi = true;
		}
		
		if(next.getType() == HaskellLexer.EOF){
			// all remaining VCCURLYs, plus two SEMIs for the road
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-EOF", next.getStopIndex()));
			while(!blocks.isEmpty()){
				popBlock();
				preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY-EOF", next.getStopIndex()));
			}
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-EOF", next.getStopIndex()));
		}else if(seenNewline){
			seenNewline = false;
			if(indent == curBlockIndent() && !noExtraSemi)
				preempted.push(createTok(HaskellLexer.SEMI, "SEMI", next.getStopIndex()));
			while(indent < curBlockIndent()){
				popBlock();
				preempted.push(createTok(HaskellLexer.SEMI, "SEMI", next.getStopIndex()));
				preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY", next.getStopIndex()));
				preempted.push(createTok(HaskellLexer.SEMI, "SEMI", next.getStopIndex()));
			}
		}
		
		wasBlockKw = switch(next.getType()){
			case HaskellLexer.LET, HaskellLexer.WHERE, HaskellLexer.DO, HaskellLexer.OF -> true;
			default -> false;
		};
		wasLetKw = next.getType() == HaskellLexer.LET;
		return preempted.pop();
	}
	
	protected int curBlockIndent(){
		if(!blocks.isEmpty())
			return blocks.peek().indent;
		return 0;
	}
	
	protected void pushBlock(int indent){
		blocks.push(new Block(indent, wasLetKw));
	}
	
	protected void popBlock(){
		blocks.pop();
	}
	
	protected void afterParen(int adj){
		if(!blocks.isEmpty())
			blocks.peek().parens += adj;
	}
	
	protected boolean isDanglingParen(){
		return !blocks.isEmpty() && blocks.peek().parens < 0;
	}
	
	protected boolean isLetBlock(){
		return !blocks.isEmpty() && blocks.peek().isLetBlock;
	}
	
	@SuppressWarnings("unchecked")
	protected Token createTok(int type, String name, int nstop){
		return /* VCCURLY */ tokenFactory.create(
				new Pair<>(this, ""),
				type,
				"<" + name + ">",
				HaskellLexer.DEFAULT_TOKEN_CHANNEL,
				nstop, nstop,
				0, 0);
	}
}