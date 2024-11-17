package polyfauna.intellihask.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.Contract;

import java.util.List;

import static org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory.createTokenSet;

public class Tokens{
	
	private static final List<TokenIElementType> TOKEN_ELEMENT_TYPES = PSIElementTypeFactory.getTokenIElementTypes(HaskellLanguage.INSTANCE);
	private static final List<RuleIElementType> RULE_ELEMENT_TYPES = PSIElementTypeFactory.getRuleIElementTypes(HaskellLanguage.INSTANCE);
	
	// most of the keywords from HaskellLexer, but *not* ones considered special_id; those need to be semantically highlighted later
	public static final TokenSet KEYWORDS = createTokenSet(
			HaskellLanguage.INSTANCE,
			
			HaskellLexer.DATA,
			HaskellLexer.CLASS,
			HaskellLexer.MODULE,
			HaskellLexer.NEWTYPE,
			HaskellLexer.INSTANCE,
			HaskellLexer.TYPE,
			
			HaskellLexer.DERIVING,
			HaskellLexer.WHERE,
			
			HaskellLexer.DEFAULT,
			HaskellLexer.IMPORT,
			HaskellLexer.INFIX,
			HaskellLexer.INFIXL,
			HaskellLexer.INFIXR,
			
			HaskellLexer.CASE,
			HaskellLexer.LCASE,
			HaskellLexer.DO,
			HaskellLexer.ELSE,
			HaskellLexer.IF,
			HaskellLexer.IN,
			HaskellLexer.LET,
			HaskellLexer.OF,
			HaskellLexer.THEN
	);
	
	public static final TokenSet WS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.WS, HaskellLexer.TAB, HaskellLexer.NEWLINE);
	public static final TokenSet COMMENTS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.COMMENT, HaskellLexer.NCOMMENT);
	public static final TokenSet STRINGS = createTokenSet(HaskellLanguage.INSTANCE, HaskellLexer.STRING);
	
	@Contract(pure = true)
	public static IElementType getFor(int type){
		return TOKEN_ELEMENT_TYPES.get(type);
	}
	
	@Contract(pure = true)
	public static IElementType getRuleFor(int type){
		return RULE_ELEMENT_TYPES.get(type);
	}
}