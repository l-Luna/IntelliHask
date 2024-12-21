package polyfauna.intellihask.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.util.Collection;
import java.util.Optional;

/**
 * An element that implicitly binds all child type variables.
 */
public interface HsTyVarBinder extends PsiElement{
	
	/**
	 * Returns whether this binder binds the type variable with the given name, or whether searching should continue upwards.
	 */
	default boolean captures(String name){
		return true;
	}
	
	default Collection<HsTyVar> findNestedTyVars(){
		return PsiTreeUtil.findChildrenOfType(this, HsTyVar.class);
	}
	
	// Collection<HsTyVar> findMyNestedTyVars(){ ... }
	
	static Optional<HsTyVarBinder> binderFor(PsiElement e, String name){
		PsiElement here = e;
		while(here != null){
			if(here instanceof HsTyVarBinder tvb && tvb.captures(name))
				return Optional.of(tvb);
			here = here.getParent();
		}
		return Optional.empty();
		//return Optional.ofNullable(PsiTreeUtil.getNonStrictParentOfType(e, HsTyVarBinder.class));
	}
}