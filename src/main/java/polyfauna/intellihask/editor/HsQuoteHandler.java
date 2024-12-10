package polyfauna.intellihask.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.antlr_generated.HaskellLexer;

import static org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory.createTokenSet;

public class HsQuoteHandler extends SimpleTokenSetQuoteHandler{
	
	public HsQuoteHandler(){
		super(
				createTokenSet(
						HaskellLanguage.INSTANCE,
						
						HaskellLexer.Quote,
						HaskellLexer.SQuote,
						HaskellLexer.STRING,
						HaskellLexer.CHAR
				)
		);
	}
}