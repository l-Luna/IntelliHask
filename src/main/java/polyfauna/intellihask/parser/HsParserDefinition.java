package polyfauna.intellihask.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsFile;
import polyfauna.intellihask.psi.file.HsLanguagePragma;
import polyfauna.intellihask.psi.file.HsModule;
import polyfauna.intellihask.psi.file.HsModuleBody;
import polyfauna.intellihask.psi.file.HsPragma;
import polyfauna.intellihask.psi.Tokens;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.jetbrains.annotations.NotNull;

public class HsParserDefinition implements ParserDefinition{
	
	public static final IFileElementType FILE = new IFileElementType(HaskellLanguage.INSTANCE);
	
	@SuppressWarnings("deprecation")
	public HsParserDefinition(){
		PSIElementTypeFactory.defineLanguageIElementTypes(HaskellLanguage.INSTANCE, HaskellLexer.tokenNames, HaskellLexer.ruleNames);
	}
	
	public @NotNull Lexer createLexer(Project project){
		return new LexerAdapter();
	}
	
	public @NotNull PsiParser createParser(Project project){
		return new ParserAdapter();
	}
	
	public @NotNull IFileElementType getFileNodeType(){
		return FILE;
	}
	
	public @NotNull TokenSet getWhitespaceTokens(){
		return Tokens.WS;
	}
	
	public @NotNull TokenSet getCommentTokens(){
		return Tokens.COMMENTS;
	}
	
	public @NotNull TokenSet getStringLiteralElements(){
		return Tokens.STRINGS;
	}
	
	public @NotNull PsiElement createElement(ASTNode node){
		IElementType type = node.getElementType();
		if(type == Tokens.getRuleFor(HaskellParser.RULE_module))
			return new HsModule(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_body))
			return new HsModuleBody(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_pragma))
			return new HsPragma(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_language_pragma))
			return new HsLanguagePragma(node);
		return new HsAstElement(node);
	}
	
	public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider){
		return new HsFile(viewProvider);
	}
}