package luna.intellihask.parser;

import luna.intellihask.HaskellLanguage;
import luna.intellihask.antlr_generated.HaskellLexer;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerState;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.misc.IntegerStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LexerAdapter extends ANTLRLexerAdaptor{
	
	public LexerAdapter(){
		super(HaskellLanguage.INSTANCE, new HaskellLexer(null));
	}
	
	public LexerAdapter(Lexer lexer){
		super(HaskellLanguage.INSTANCE, lexer);
	}
}