package polyfauna.intellihask.psi.expr;

import com.intellij.lang.ASTNode;
import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsSymbolReference;
import polyfauna.intellihask.psi.symbol.BindingSymbol;
import polyfauna.intellihask.psi.symbol.searcher.BindingSearcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HsVar extends HsSymbolReference{
	
	public HsVar(@NotNull ASTNode node){
		super(node);
	}
	
	public String name(){
		return getText().replace(" ", "").replace("(", "").replace(")", "");
	}
	
	public @NotNull PsiElement getElement(){
		return this;
	}
	
	public @NotNull TextRange getRangeInElement(){
		return TextRange.from(0, getTextLength());
	}
	
	public @NotNull Collection<? extends @NotNull PsiSymbolReference> getOwnReferences(){
		return List.of(this);
	}
	
	public @NotNull Collection<? extends Symbol> resolveReference(){
		return BindingSearcher.byName(getProject(), name()).collect(Collectors.toSet());
	}
	
	public boolean resolvesTo(@NotNull Symbol target){
		return target instanceof BindingSymbol bs && bs.name().equals(name()) && /* scope */ super.resolvesTo(target);
	}
}