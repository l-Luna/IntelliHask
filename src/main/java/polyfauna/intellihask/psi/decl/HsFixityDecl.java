package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

public class HsFixityDecl extends HsAstElement implements HsDecl{
	
	public HsFixityDecl(@NotNull ASTNode node){
		super(node);
	}
}