package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

public class HsInstanceDecl extends HsAstElement implements HsDecl{
	
	public HsInstanceDecl(@NotNull ASTNode node){
		super(node);
	}
}