package polyfauna.intellihask.psi.type;

import com.intellij.lang.ASTNode;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsSymbolReference;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.symbol.TyVarSymbol;

import java.util.Collection;
import java.util.List;

public class HsTyVar extends HsSymbolReference{
	
	public HsTyVar(@NotNull ASTNode node){
		super(node);
	}
	
	public String name(){
		return getText();
	}
	
	public @NotNull Collection<? extends @NotNull PsiSymbolReference> getOwnReferences(){
		return List.of(this);
	}
	
	public @NotNull PsiElement getElement(){
		return this;
	}
	
	public @NotNull TextRange getRangeInElement(){
		return TextRange.from(0, getTextLength());
	}
	
	public @NotNull Collection<TyVarSymbol> resolveReference(){
		String name = name();
		return HsTyVarBinder.binderFor(this, name).map(binder -> List.of(new TyVarSymbol(name, binder))).orElseGet(List::of);
	}
}