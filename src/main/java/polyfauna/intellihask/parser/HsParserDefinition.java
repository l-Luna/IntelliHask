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
import polyfauna.intellihask.psi.decl.*;
import polyfauna.intellihask.psi.decl.ctx.HsCDecl;
import polyfauna.intellihask.psi.decl.ctx.HsIDecl;
import polyfauna.intellihask.psi.decl.ctx.HsTopDecl;
import polyfauna.intellihask.psi.expr.HsDVar;
import polyfauna.intellihask.psi.expr.HsDVars;
import polyfauna.intellihask.psi.expr.HsUVar;
import polyfauna.intellihask.psi.file.*;
import polyfauna.intellihask.psi.Tokens;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.pattern.HsApat;
import polyfauna.intellihask.psi.pattern.HsPattern;
import polyfauna.intellihask.psi.type.HsTyVar;
import polyfauna.intellihask.psi.type.HsType;

public class HsParserDefinition implements ParserDefinition{
	
	public static final IFileElementType FILE = new IFileElementType(HaskellLanguage.INSTANCE);
	
	public HsParserDefinition(){
		defineElementTypes();
	}
	
	@SuppressWarnings("deprecation")
	/* local */ static void defineElementTypes(){
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
		// .expr
		if(type == Tokens.getRuleFor(HaskellParser.RULE_dvar) || type == Tokens.getRuleFor(HaskellParser.RULE_dqvar))
			return new HsDVar(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_uvar) || type == Tokens.getRuleFor(HaskellParser.RULE_uqvar))
			return new HsUVar(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_dvars))
			return new HsDVars(node);
		// .file
		if(type == Tokens.getRuleFor(HaskellParser.RULE_module))
			return new HsModule(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_body))
			return new HsModuleBody(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_pragma))
			return new HsPragma(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_language_pragma))
			return new HsLanguagePragma(node);
		// .decl
		if(type == Tokens.getRuleFor(HaskellParser.RULE_classdecl))
			return new HsClassDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_datadecl))
			return new HsDataDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_defaultdecl))
			return new HsDefaultDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_fixitydecl))
			return new HsFixityDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_instancedecl))
			return new HsInstanceDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_newtypedecl))
			return new HsNewtypeDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_typealiasdecl))
			return new HsTypeAliasDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_vartypedecl))
			return new HsVarTypeDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_gendecls))
			return new HsGenDecls(node);
		// .decl.ctx
		if(type == Tokens.getRuleFor(HaskellParser.RULE_cdecl))
			return new HsCDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_gendecl))
			return new HsIDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_idecl))
			return new HsIDecl(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_topdecl))
			return new HsTopDecl(node);
		// .pattern
		if(type == Tokens.getRuleFor(HaskellParser.RULE_pat))
			return new HsPattern(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_apat))
			return new HsApat(node);
		// .type
		if(type == Tokens.getRuleFor(HaskellParser.RULE_type))
			return new HsType(node);
		if(type == Tokens.getRuleFor(HaskellParser.RULE_tyvar))
			return new HsTyVar(node);
		return new HsAstElement(node);
	}
	
	public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider){
		return new HsFile(viewProvider);
	}
}