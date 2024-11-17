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
	
	/*protected ANTLRLexerState getInitialState(){
		return new HsLexState(Lexer.DEFAULT_MODE, null, HaskellBaseLexer.initState());
	}
	
	protected ANTLRLexerState getLexerState(Lexer lexer){
		return new HsLexState(lexer._mode, lexer._modeStack.isEmpty() ? null : lexer._modeStack, ((HaskellBaseLexer)lexer).toState());
	}
	
	private static class HsLexState extends ANTLRLexerState{
		
		HaskellBaseLexer.HsLexerState inS;
		
		public HsLexState(int mode, @Nullable IntegerStack modeStack, HaskellBaseLexer.HsLexerState inS){
			super(mode, modeStack);
			this.inS = inS;
		}
		
		public void apply(@NotNull Lexer lexer){
			super.apply(lexer);
			((HaskellBaseLexer)lexer).applyState(inS);
		}
		
		public boolean equals(Object obj){
			return super.equals(obj) && obj instanceof HsLexState other && other.inS.equals(inS);
		}
		
		protected int hashCodeImpl(){
			return super.hashCodeImpl() ^ inS.hashCode();
		}
	}*/
}