package polyfauna.intellihask.parser;

import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.v4.runtime.Lexer;

public class LexerAdapter extends ANTLRLexerAdaptor{
	
	public LexerAdapter(){
		super(HaskellLanguage.INSTANCE, new HaskellLexer(null));
	}
	
	public LexerAdapter(Lexer lexer){
		super(HaskellLanguage.INSTANCE, lexer);
	}
}