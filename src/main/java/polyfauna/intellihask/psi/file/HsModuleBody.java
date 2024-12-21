package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsBindingOwner;
import polyfauna.intellihask.psi.decl.HsDecl;
import polyfauna.intellihask.psi.decl.HsDeclGroup;
import polyfauna.intellihask.psi.decl.ctx.HsDeclCtx;
import polyfauna.intellihask.psi.decl.ctx.HsTopDecl;

import java.util.List;

public class HsModuleBody extends HsAstElement implements HsDeclGroup{
	
	public HsModuleBody(@NotNull ASTNode node){
		super(node);
	}
	
	public BindingType bindingType(){
		return BindingType.GROUP;
	}
}