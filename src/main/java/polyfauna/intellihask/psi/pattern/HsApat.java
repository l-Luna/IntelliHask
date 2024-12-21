package polyfauna.intellihask.psi.pattern;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsBindingOwner;

public class HsApat extends HsAstElement implements HsBindingOwner{
	
	public HsApat(@NotNull ASTNode node){
		super(node);
	}
	
	public BindingType bindingType(){
		return BindingType.PATTERN;
	}
}