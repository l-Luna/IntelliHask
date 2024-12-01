package polyfauna.intellihask.psi.symbol;

import com.intellij.find.usages.api.SearchTarget;
import com.intellij.find.usages.api.UsageHandler;
import com.intellij.model.Pointer;
import com.intellij.navigation.NavigatableSymbol;
import com.intellij.navigation.SymbolNavigationService;
import com.intellij.openapi.project.Project;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.navigation.NavigationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.refactoring.rename.api.RenameTarget;
import com.intellij.refactoring.rename.api.RenameValidationResult;
import com.intellij.refactoring.rename.api.RenameValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.util.Collection;
import java.util.List;

public record TyVarSymbol(String name, HsTyVarBinder owner) implements
		HsSymbol, NavigatableSymbol,
		DocumentationTarget, SearchTarget, RenameTarget{
	
	public @NotNull Pointer<TyVarSymbol> createPointer(){
		return new SPointer(name, SmartPointerManager.createPointer(owner));
	}
	
	public Class<HsTyVar> psiType(){
		return HsTyVar.class;
	}
	
	// navigation
	
	public @NotNull Collection<? extends NavigationTarget> getNavigationTargets(@NotNull Project project){
		return owner.findNestedTyVars().stream()
				.filter(x -> x.resolveReference().equals(List.of(this)))
				.map(SymbolNavigationService.getInstance()::psiElementNavigationTarget)
				.toList();
	}
	
	// documentation/presentation
	
	public @NotNull TargetPresentation computePresentation(){
		return TargetPresentation.builder("").presentation();
	}
	
	public @NotNull DocumentationResult computeDocumentation(){
		return DocumentationResult.documentation("type variable <strong><samp>%s</samp></strong>".formatted(name));
	}
	
	// rename
	
	public @NotNull String getTargetName(){
		return name;
	}
	
	public @NotNull TargetPresentation presentation(){
		return computePresentation();
	}
	
	public @NotNull SearchScope getMaximalSearchScope(){
		return new LocalSearchScope(owner);
	}
	
	public @NotNull RenameValidator validator(){
		return cand -> cand.isBlank() || Character.isUpperCase(cand.charAt(0))
				? RenameValidationResult.invalid()
				: RenameValidationResult.ok();
	}
	
	// search
	
	public @NotNull UsageHandler getUsageHandler(){
		return UsageHandler.createEmptyUsageHandler("Type Variable '%s'".formatted(name));
	}
	
	
	@Override
	public String toString(){
		return "TyVarSymbol[" +
				"name=" + name + ", " +
				"owner=" + owner + ']';
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