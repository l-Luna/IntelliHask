package polyfauna.intellihask.psi;

import com.intellij.psi.PsiElement;
import polyfauna.intellihask.language.decls.BindingType;

/**
 * An element that binds child variable definitions. A child {@linkplain polyfauna.intellihask.psi.expr.HsDVar}
 * will define its type and behaviour based on the nearest {@code HsBindingOwner} parent.
 */
public interface HsBindingOwner extends PsiElement{

	BindingType bindingType();
}