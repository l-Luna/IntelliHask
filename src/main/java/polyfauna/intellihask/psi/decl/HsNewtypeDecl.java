package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsBindingOwner;
import polyfauna.intellihask.psi.HsTyVarBinder;

public class HsNewtypeDecl extends HsAstElement implements HsDecl, HsTyVarBinder, HsBindingOwner{
	
	public HsNewtypeDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public BindingType bindingType(){
		return BindingType.FIELD;
	}
}