package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;

public class HsGenDecls extends HsAstElement implements HsDeclGroup{
	
	public HsGenDecls(@NotNull ASTNode node){
		super(node);
	}
	
	public BindingType bindingType(){
		return BindingType.GROUP;
	}
}