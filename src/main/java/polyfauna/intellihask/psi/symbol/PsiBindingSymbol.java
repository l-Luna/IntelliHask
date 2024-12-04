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
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.language.type.Type;
import polyfauna.intellihask.psi.decl.HsDecl;
import polyfauna.intellihask.psi.decl.HsNamedDecl;
import polyfauna.intellihask.psi.decl.HsVarTypeDecl;
import polyfauna.intellihask.psi.file.HsModule;
import polyfauna.intellihask.psi.type.HsType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A {@linkplain BindingSymbol} backed by a PSI element.
 */
public record PsiBindingSymbol(String name, HsModule owner)
		implements BindingSymbol,
		NavigatableSymbol, DocumentationTarget, SearchTarget{
	
	// note that we *physically* point so a HsModule; the pointer class checks that the specific
	// symbol actually still exists
	
	public String name(){
		return name;
	}
	
	public SearchScope getMaximalSearchScope(){
		return GlobalSearchScope.allScope(owner.getProject());
	}
	
	public @NotNull Pointer<PsiBindingSymbol> createPointer(){
		return new SPointer(name, SmartPointerManager.createPointer(owner));
	}
	
	public Optional<Type> type(){
		for(HsDecl decl : owner.decls())
			if(decl instanceof HsVarTypeDecl vtd && vtd.names().contains(name))
				return vtd.type().flatMap(HsType::rep);
		return Optional.empty();
	}
	
	public @NotNull UsageHandler getUsageHandler(){
		return UsageHandler.createEmptyUsageHandler("Binding '%s'".formatted(name));
	}
	
	public @NotNull TargetPresentation presentation(){
		return computePresentation();
	}
	
	public @NotNull TargetPresentation computePresentation(){
		return TargetPresentation.builder("").presentation();
	}
	
	public @NotNull DocumentationResult computeDocumentation(){
		return DocumentationResult.documentation("binding <strong><samp>%s</samp></strong>".formatted(name));
	}
	
	public @NotNull Collection<? extends NavigationTarget> getNavigationTargets(@NotNull Project project){
		return owner.decls().stream()
				.filter(HsNamedDecl.class::isInstance)
				.map(HsNamedDecl.class::cast)
				.flatMap(x->x.vars().stream().filter(v -> v.name().equals(name)).findFirst().stream())
				.findFirst()
				.map(SymbolNavigationService.getInstance()::psiElementNavigationTarget)
				.map(List::of)
				.orElse(List.of());
	}
	
	private record SPointer(String name, SmartPsiElementPointer<HsModule> ownerP) implements Pointer<PsiBindingSymbol>{
		
		public @Nullable PsiBindingSymbol dereference(){
			var owner = ownerP.dereference();
			// check if the symbol is still valid too
			if(owner == null)
				return null;
			for(HsDecl decl : owner.decls())
				if(decl instanceof HsNamedDecl hnd && hnd.names().contains(name))
					return new PsiBindingSymbol(name, owner);
			return null;
		}
	}
}