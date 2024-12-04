package polyfauna.intellihask.psi.expr;

import com.intellij.lang.ASTNode;
import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsFile;
import polyfauna.intellihask.psi.HsSymbolReference;
import polyfauna.intellihask.psi.decl.HsNamedDecl;
import polyfauna.intellihask.psi.symbol.PsiBindingSymbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
		// resolve to a binding...
		// which may come from any module in the project, plus from any module providers
		// let's keep it simple here & generify after
		// TODO: everything
		List<PsiBindingSymbol> symbols = new ArrayList<>();
		String name = name();
		ProjectRootManager.getInstance(getProject()).getFileIndex().iterateContent(
				file -> {
					PsiFile pFile = PsiManager.getInstance(getProject()).findFile(file);
					if(!(pFile instanceof HsFile hsf))
						return true;
					hsf.module().ifPresent(module -> {
						if(module.decls().stream().anyMatch(x -> x instanceof HsNamedDecl hnd && hnd.names().contains(name)))
							symbols.add(new PsiBindingSymbol(name, module));
					});
					return true;
				},
				file -> Objects.equals(file.getExtension(), "hs"));
		return symbols;
	}
}