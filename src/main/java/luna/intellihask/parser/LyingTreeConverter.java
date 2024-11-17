package luna.intellihask.parser;

import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import luna.intellihask.antlr_generated.HaskellLexer;
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.TerminalNode;

public class LyingTreeConverter extends ANTLRParseTreeToPSIConverter{
	
	public LyingTreeConverter(Language language, Parser parser, PsiBuilder builder){
		super(language, parser, builder);
	}
	
	public void visitTerminal(TerminalNode node){
		int type = node.getSymbol().getType();
		if(type != HaskellLexer.SEMI && type != HaskellLexer.VCCURLY && type != HaskellLexer.VOCURLY)
			super.visitTerminal(node);
	}
	
	public void exitEveryRule(ParserRuleContext ctx){
		ProgressIndicatorProvider.checkCanceled();
		PsiBuilder.Marker marker = markers.pop();
		// if the rule has an error and no children, then an error node was not properly created
		// have to do it ourselves...
		RecognitionException e = ctx.exception;
		if(ctx.getChildCount() == 0 && e != null){
			// simplified from DefaultErrorStrategy::reportInputMismatch
			marker.error("mismatched input %s expecting %s".formatted(e.getOffendingToken().getText(), e.getExpectedTokens().toString(e.getRecognizer().getVocabulary())));
		}else
			marker.done(getRuleElementTypes().get(ctx.getRuleIndex()));
	}
}