package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsBindingOwner;

public class HsInstanceDecl extends HsAstElement implements HsDecl, HsDeclGroup{
	
	public HsInstanceDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public BindingType bindingType(){
		return BindingType.GROUP;
	}
}