package luna.intellihask.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import luna.intellihask.HaskellLanguage;
import luna.intellihask.antlr_generated.HaskellLexer;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.Contract;

import java.util.List;

import static org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory.createTokenSet;

public class Tokens{
	
	private static final List<TokenIElementType> TOKEN_ELEMENT_TYPES = PSIElementTypeFactory.getTokenIElementTypes(HaskellLanguage.INSTANCE);
	
	public static final TokenSet KEYWORDS = createTokenSet(
			HaskellLanguage.INSTANCE,
			
			HaskellLexer.DATA,
			HaskellLexer.CLASS
	);
	
	public static final TokenSet WS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.WS, HaskellLexer.TAB, HaskellLexer.NEWLINE);
	public static final TokenSet COMMENTS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.COMMENT, HaskellLexer.NCOMMENT);
	public static final TokenSet STRINGS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.STRING);
	
	@Contract(pure = true)
	public static IElementType getFor(int type){
		return TOKEN_ELEMENT_TYPES.get(type);
	}
}