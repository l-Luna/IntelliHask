package polyfauna.intellihask.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.Trees;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HsAstElement extends ASTWrapperPsiElement{
	
	public HsAstElement(@NotNull ASTNode node){
		super(node);
	}
	
	public PsiElement @NotNull [] getChildren(){
		// include leaves
		return Trees.getChildren(this);
	}
	
	public List<HsAstElement> getChildrenOfAstType(IElementType type){
		List<HsAstElement> ret = new ArrayList<>();
		for(PsiElement child : getChildren())
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				ret.add(hae);
		return ret;
	}
	
	public int countChildrenOfAstType(IElementType type){
		int c = 0;
		for(PsiElement child : getChildren())
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				c++;
		return c;
	}
	
	public boolean hasChildOfAstType(IElementType type){
		for(PsiElement child : getChildren())
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				return true;
		return false;
	}
	
	public String toString(){
		if(getClass() != HsAstElement.class)
			return getClass().getSimpleName();
		boolean isRule = getNode().getElementType() instanceof RuleIElementType;
		if(isRule && ((RuleIElementType)getNode().getElementType()).getRuleIndex() < HaskellParser.ruleNames.length)
			return getClass().getSimpleName() + "(" + HaskellParser.ruleNames[((RuleIElementType)getNode().getElementType()).getRuleIndex()] + ")";
		return getClass().getSimpleName();
	}
	
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place){
		boolean cont = true;
		for(PsiElement child : getChildren())
			if(cont && lastParent != child)
				cont = processor.execute(child, state);
		return cont;
	}
}