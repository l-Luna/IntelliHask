package polyfauna.intellihask.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.util.Collection;
import java.util.Optional;

/**
 * A syntax element that implicitly binds all child type variables.
 */
// TODO: conditionally stop searching, for explicit foralls
public interface HsTyVarBinder extends PsiElement{

	default Collection<HsTyVar> findNestedTyVars(){
		return PsiTreeUtil.findChildrenOfType(this, HsTyVar.class);
	}
	
	// Collection<HsTyVar> findMyNestedTyVars(){ ... }
	
	static Optional<HsTyVarBinder> binderFor(PsiElement e){
		return Optional.ofNullable(PsiTreeUtil.getNonStrictParentOfType(e, HsTyVarBinder.class));
	}
}