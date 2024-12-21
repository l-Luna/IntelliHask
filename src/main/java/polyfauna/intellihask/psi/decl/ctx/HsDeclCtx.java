package polyfauna.intellihask.psi.decl.ctx;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.psi.decl.HsDecl;

import java.util.Optional;

public interface HsDeclCtx extends PsiElement{
	
	default Optional<HsDecl> inner(){
		return Optional.ofNullable(PsiTreeUtil.getChildOfType(this, HsDecl.class));
	}
}