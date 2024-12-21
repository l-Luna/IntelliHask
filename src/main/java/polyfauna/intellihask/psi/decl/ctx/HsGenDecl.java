package polyfauna.intellihask.psi.decl.ctx;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

public class HsGenDecl extends HsAstElement implements HsDeclCtx{
	
	public HsGenDecl(@NotNull ASTNode node){
		super(node);
	}
}