package polyfauna.intellihask.psi.symbol;

import com.intellij.model.Pointer;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.language.type.Type;
import polyfauna.intellihask.psi.expr.HsDVar;

import java.util.Optional;

/**
 * A {@linkplain BindingSymbol} for a pattern binding, backed by a PSI element.
 */
public record PsiPatternBindingSymbol(String name, HsDVar owner)
	implements BindingSymbol{
	
	public @NotNull Pointer<PsiPatternBindingSymbol> createPointer(){
		return new SPointer(name, SmartPointerManager.createPointer(owner));
	}
	
	public Optional<Type> type(){
		return Optional.empty();
	}
	
	public SearchScope getMaximalSearchScope(){
		// TODO: can be reduced further
		// ...or expanded, in the case of exported record fields
		return new LocalSearchScope(owner.getContainingFile());
	}
	
	private record SPointer(String name, SmartPsiElementPointer<HsDVar> ownerP) implements Pointer<PsiPatternBindingSymbol>{
		
		public @Nullable PsiPatternBindingSymbol dereference(){
			var owner = ownerP.dereference();
			if(owner != null && owner.textMatches(name))
				return new PsiPatternBindingSymbol(name, owner);
			return null;
		}
	}
}