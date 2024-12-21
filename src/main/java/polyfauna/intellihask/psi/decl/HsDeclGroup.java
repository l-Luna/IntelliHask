package polyfauna.intellihask.psi.decl;

import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.psi.HsBindingOwner;
import polyfauna.intellihask.psi.decl.ctx.HsDeclCtx;

import java.util.List;

/**
 * Corresponds to binding groups: {@code decls}, {@code idecls}, {@code cdecls} in the grammar,
 * or {@code where}/{@code let} blocks more generally in practice.
 */
public interface HsDeclGroup extends HsBindingOwner{
	
	default List<HsDecl> decls(){
		return PsiTreeUtil.getChildrenOfTypeAsList(this, HsDeclCtx.class).stream().flatMap(x -> x.inner().stream()).toList();
	}
}