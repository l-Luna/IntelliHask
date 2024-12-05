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
	
	protected Deque<Token> preempted = new ArrayDeque<>();
	protected Deque<Integer> blocks = new ArrayDeque<>();
	protected boolean wasBlockKw = false;
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
		// any block kw not followed by {, even if there's no newline
		boolean justStartedBlock = false;
		if(wasBlockKw && next.getType() != HaskellLexer.OCURLY){
			blocks.push(indent);
			preempted.push(createTok(HaskellLexer.VOCURLY, "VOCURLY", next.getStopIndex()));
			justStartedBlock = true;
		}
		if(next.getType() == HaskellLexer.EOF){
			// all remaining VCCURLYs, plus two SEMIs for the road
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-EOF", next.getStopIndex()));
			while(!blocks.isEmpty()){
				blocks.pop();
				preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY-EOF", next.getStopIndex()));
			}
			preempted.push(createTok(HaskellLexer.SEMI, "SEMI-EOF", next.getStopIndex()));
		}else if(seenNewline){
			seenNewline = false;
			if(indent == curBlockIndent() && !justStartedBlock)
				preempted.push(createTok(HaskellLexer.SEMI, "SEMI", next.getStopIndex()));
			while(indent < curBlockIndent()){
				blocks.pop();
				preempted.push(createTok(HaskellLexer.SEMI, "SEMI", next.getStopIndex()));
				preempted.push(createTok(HaskellLexer.VCCURLY, "VCCURLY", next.getStopIndex()));
			}
		}
		wasBlockKw = switch(next.getType()){
			case HaskellLexer.LET, HaskellLexer.WHERE, HaskellLexer.DO, HaskellLexer.OF -> true;
			default -> false;
		};
		return preempted.pop();
	}
	
	protected int curBlockIndent(){
		if(!blocks.isEmpty())
			return blocks.peek();
		return 0;
	}
	
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