package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.lang.Language;
import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.model.search.CodeReferenceSearcher;
import com.intellij.model.search.LeafOccurrence;
import com.intellij.model.search.SearchRequest;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.symbol.TyVarSymbol;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.util.Collection;
import java.util.List;

public class HsCodeRefSearcher implements CodeReferenceSearcher{
	
	public @NotNull Language getReferencingLanguage(@NotNull Symbol target){
		return HaskellLanguage.INSTANCE;
	}
	
	public @Nullable SearchRequest getSearchRequest(@NotNull Project project, @NotNull Symbol target){
		if(target instanceof TyVarSymbol(String name, var owner))
			return SearchRequest.of(name, new LocalSearchScope(owner));
		return null;
	}
	
	public @NotNull Collection<? extends @NotNull PsiSymbolReference> getReferences(@NotNull Symbol target, @NotNull LeafOccurrence occurrence){
		if(target instanceof TyVarSymbol tv){
			HsTyVar elem = PsiTreeUtil.getParentOfType(occurrence.getStart(), HsTyVar.class, false, /* TODO(RankNTypes) */ HsTyVarBinder.class);
			if(elem != null && elem.resolvesTo(target))
				return List.of(elem);
		}
		return List.of();
	}
}