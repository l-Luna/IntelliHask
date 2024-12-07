package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

public class HsDefaultDecl extends HsAstElement implements HsDecl{
	
	public HsDefaultDecl(@NotNull ASTNode node){
		super(node);
	}
}