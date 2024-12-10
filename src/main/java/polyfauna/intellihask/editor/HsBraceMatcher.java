package polyfauna.intellihask.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import polyfauna.intellihask.psi.Tokens;

public class HsBraceMatcher implements PairedBraceMatcher{
	
	public BracePair @NotNull [] getPairs(){
		return new BracePair[]{
				new BracePair(Tokens.getFor(HaskellLexer.OpenRoundBracket), Tokens.getFor(HaskellLexer.CloseRoundBracket), false),
				new BracePair(Tokens.getFor(HaskellLexer.OpenSquareBracket), Tokens.getFor(HaskellLexer.CloseSquareBracket), false),
				new BracePair(Tokens.getFor(HaskellLexer.OpenPragmaBracket), Tokens.getFor(HaskellLexer.ClosePragmaBracket), false),
				new BracePair(Tokens.getFor(HaskellLexer.OpenBoxParen), Tokens.getFor(HaskellLexer.CloseBoxParen), false),
				new BracePair(Tokens.getFor(HaskellLexer.OCURLY), Tokens.getFor(HaskellLexer.CCURLY), false)
		};
	}
	
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType){
		return true;
	}
	
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset){
		return openingBraceOffset;
	}
}