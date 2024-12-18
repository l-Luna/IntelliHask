package polyfauna.intellihask.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.Trees;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HsAstElement extends ASTWrapperPsiElement{
	
	public HsAstElement(@NotNull ASTNode node){
		super(node);
	}
	
	/**
	 * Returns all children of this node, including leaf elements.
	 */
	public PsiElement @NotNull [] getChildren(){
		// include leaves
		return Trees.getChildren(this);
	}
	
	public Optional<HsAstElement> getChildOfAstType(IElementType type){
		for(PsiElement child : getChildren())
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				return Optional.of(hae);
			/*if(child instanceof LeafPsiElement leaf && leaf.getElementType() == type)
				return Optional.of(leaf);*/
		return Optional.empty();
	}
	
	public List<HsAstElement> getChildrenOfAstType(IElementType type){
		List<HsAstElement> ret = new ArrayList<>();
		for(PsiElement child : getChildren())
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				ret.add(hae);
			/*if(child instanceof LeafPsiElement leaf && leaf.getElementType() == type)
				ret.add(leaf);*/
		return ret;
	}
	
	public int countChildrenOfAstType(IElementType type){
		int c = 0;
		for(PsiElement child : getChildren()){
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				c++;
			if(child instanceof LeafPsiElement leaf && leaf.getElementType() == type)
				c++;
		}
		return c;
	}
	
	public boolean hasChildOfAstType(IElementType type){
		for(PsiElement child : getChildren()){
			if(child instanceof HsAstElement hae && hae.getNode().getElementType() == type)
				return true;
			if(child instanceof LeafPsiElement leaf && leaf.getElementType() == type)
				return true;
		}
		return false;
	}
	
	public <T> Optional<T> getChildOfType(Class<T> type){
		for(PsiElement child : getChildren())
			if(type.isInstance(child))
				return Optional.of(type.cast(child));
		return Optional.empty();
	}
	
	public <T> List<T> getChildrenOfType(Class<T> type){
		List<T> ret = new ArrayList<>();
		for(PsiElement child : getChildren())
			if(type.isInstance(child))
				ret.add(type.cast(child));
		return ret;
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