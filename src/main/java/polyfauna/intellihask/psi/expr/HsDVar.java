package polyfauna.intellihask.psi.expr;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiCompletableReference;
import com.intellij.model.psi.PsiSymbolDeclaration;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsBindingOwner;
import polyfauna.intellihask.psi.HsSymbolReference;
import polyfauna.intellihask.psi.decl.HsDeclGroup;
import polyfauna.intellihask.psi.symbol.BindingSymbol;
import polyfauna.intellihask.psi.symbol.PsiGroupedBindingSymbol;
import polyfauna.intellihask.psi.symbol.PsiPatternBindingSymbol;
import polyfauna.intellihask.psi.symbol.searcher.BindingSearcher;

import java.util.Collection;
import java.util.List;

public class HsDVar extends HsSymbolReference implements PsiCompletableReference, PsiSymbolDeclaration{
	
	public HsDVar(@NotNull ASTNode node){
		super(node);
	}
	
	public String name(){
		return getText().replace(" ", "").replace("(", "").replace(")", "");
	}
	
	public @NotNull PsiElement getElement(){
		return this;
	}
	
	public @NotNull PsiElement getDeclaringElement(){
		return this;
	}
	
	public @NotNull TextRange getRangeInElement(){
		return TextRange.from(0, getTextLength());
	}
	
	public @NotNull TextRange getRangeInDeclaringElement(){
		return getRangeInElement();
	}
	
	public @NotNull TextRange getAbsoluteRange(){
		return super.getAbsoluteRange();
	}
	
	public @NotNull Collection<? extends @NotNull PsiSymbolReference> getOwnReferences(){
		return List.of(this);
	}
	
	public @NotNull Collection<? extends Symbol> resolveReference(){
		return List.of(getSymbol());
	}
	
	public @NotNull Symbol getSymbol(){
		HsBindingOwner owner = PsiTreeUtil.getParentOfType(this, HsBindingOwner.class);
		return owner instanceof HsDeclGroup hdg
				? new PsiGroupedBindingSymbol(name(), hdg)
				: new PsiPatternBindingSymbol(name(), this);
	}
	
	public @NotNull Collection<@NotNull LookupElement> getCompletionVariants(){
		return BindingSearcher.topLevel(getProject())
				.distinct()
				.map(BindingSymbol::describeLookup)
				.toList();
	}
	
	public boolean resolvesTo(@NotNull Symbol target){
		return target instanceof BindingSymbol bs && bs.name().equals(name()) && /* scope */ super.resolvesTo(target);
	}
}