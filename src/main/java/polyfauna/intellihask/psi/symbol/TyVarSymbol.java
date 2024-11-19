package polyfauna.intellihask.psi.symbol;

import com.intellij.model.Pointer;
import com.intellij.model.Symbol;
import com.intellij.navigation.NavigatableSymbol;
import com.intellij.navigation.SymbolNavigationService;
import com.intellij.openapi.project.Project;
import com.intellij.platform.backend.navigation.NavigationTarget;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.psi.HsTyVarBinder;

import java.util.Collection;
import java.util.List;

public record TyVarSymbol(String name, HsTyVarBinder owner) implements Symbol, NavigatableSymbol{
	
	public @NotNull Pointer<? extends Symbol> createPointer(){
		return new SPointer(name, SmartPointerManager.createPointer(owner));
	}
	
	public @NotNull Collection<? extends NavigationTarget> getNavigationTargets(@NotNull Project project){
		return owner.findNestedTyVars().stream()
				.filter(x -> x.resolveReference().equals(List.of(this)))
				.map(SymbolNavigationService.getInstance()::psiElementNavigationTarget)
				.toList();
	}
	
	protected static class SPointer implements Pointer<TyVarSymbol>{
		
		private final String name;
		private final SmartPsiElementPointer<HsTyVarBinder> ownerP;
		
		protected SPointer(String name, SmartPsiElementPointer<HsTyVarBinder> ownerP){
			this.name = name;
			this.ownerP = ownerP;
		}
		
		public @Nullable TyVarSymbol dereference(){
			var owner = ownerP.dereference();
			return owner != null ? new TyVarSymbol(name, owner) : null;
		}
	}
}