package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.decl.HsDecl;
import polyfauna.intellihask.psi.decl.ctx.HsTopDecl;

import java.util.List;

public class HsModuleBody extends HsAstElement{
	
	public HsModuleBody(@NotNull ASTNode node){
		super(node);
	}
	
	public List<HsDecl> decls(){
		return getChildrenOfType(HsTopDecl.class).stream().flatMap(x -> x.inner().stream()).toList();
	}
}