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
import polyfauna.intellihask.psi.decl.HsDeclGroup;
import polyfauna.intellihask.psi.decl.HsNamedDecl;
import polyfauna.intellihask.psi.decl.HsVarTypeDecl;
import polyfauna.intellihask.psi.type.HsType;

import java.util.Collection;
import java.util.Optional;

/**
 * A {@linkplain BindingSymbol} for a binding in a group, backed by PSI elements.
 * <p>
 * Grouped bindings may have multiple definitions and be defined mutually recursively with other
 * bindings in the group; a grouped symbol points to the binding group that it is defined in.
 */
public record PsiGroupedBindingSymbol(String name, HsDeclGroup owner)
		implements BindingSymbol,
		NavigatableSymbol, DocumentationTarget, SearchTarget{
	
	public SearchScope getMaximalSearchScope(){
		// TODO: can usually be reduced
		// (e.g. non-exported top-level items to the file scope, and local items further)
		return GlobalSearchScope.allScope(owner.getProject());
	}
	
	public @NotNull Pointer<PsiGroupedBindingSymbol> createPointer(){
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
		return DocumentationResult.documentation("binding <samp><strong>%s</strong> :: %s</samp>".formatted(name, type().map(Type::pretty).orElse("??")));
	}
	
	public @NotNull Collection<? extends NavigationTarget> getNavigationTargets(@NotNull Project project){
		return owner.decls().stream()
				.flatMap(HsDecl::innerDecls)
				.filter(HsNamedDecl.class::isInstance)
				.map(HsNamedDecl.class::cast)
				.flatMap(x -> x.vars().stream().filter(v -> v.name().equals(name)).findFirst().stream())
				.map(SymbolNavigationService.getInstance()::psiElementNavigationTarget)
				.toList();
	}
	
	private record SPointer(String name, SmartPsiElementPointer<HsDeclGroup> ownerP) implements Pointer<PsiGroupedBindingSymbol>{
		
		public @Nullable PsiGroupedBindingSymbol dereference(){
			HsDeclGroup owner = ownerP.dereference();
			// check if the symbol is still valid too
			if(owner == null)
				return null;
			for(HsDecl declT : owner.decls())
				for(HsDecl decl : declT.innerDecls().toList())
					if(decl instanceof HsNamedDecl hnd && hnd.names().contains(name))
						return new PsiGroupedBindingSymbol(name, owner);
			return null;
		}
	}
}