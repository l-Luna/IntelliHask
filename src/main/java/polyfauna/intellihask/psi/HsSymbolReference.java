package polyfauna.intellihask.psi;

import com.intellij.lang.ASTNode;
import com.intellij.model.psi.PsiSymbolReference;
import org.jetbrains.annotations.NotNull;

public abstract class HsSymbolReference extends HsAstElement implements PsiSymbolReference{
	
	public HsSymbolReference(@NotNull ASTNode node){
		super(node);
	}
}