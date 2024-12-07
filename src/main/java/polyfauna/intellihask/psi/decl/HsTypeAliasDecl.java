package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;

public class HsTypeAliasDecl extends HsAstElement implements HsDecl, HsTyVarBinder{
	
	public HsTypeAliasDecl(@NotNull ASTNode node){
		super(node);
	}
}