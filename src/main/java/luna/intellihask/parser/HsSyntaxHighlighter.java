package luna.intellihask.parser;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import luna.intellihask.antlr_generated.HaskellLexer;
import luna.intellihask.psi.Tokens;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HsSyntaxHighlighter extends SyntaxHighlighterFactory implements SyntaxHighlighter{
	
	public static final TextAttributesKey KEYWORD = DefaultLanguageHighlighterColors.KEYWORD;
	public static final TextAttributesKey COMMENT = DefaultLanguageHighlighterColors.LINE_COMMENT;
	
	public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile){
		return new HsSyntaxHighlighter();
	}
	
	public @NotNull Lexer getHighlightingLexer(){
		return new LexerAdapter();
	}
	
	public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tt){
		if(Tokens.KEYWORDS.contains(tt))
			return arr(KEYWORD);
		else if(Tokens.COMMENTS.contains(tt))
			return arr(COMMENT);
		
		return new TextAttributesKey[0];
	}
	
	private TextAttributesKey[] arr(TextAttributesKey key){
		return new TextAttributesKey[]{key};
	}
}