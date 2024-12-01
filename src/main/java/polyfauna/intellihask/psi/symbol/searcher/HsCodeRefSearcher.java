package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.lang.Language;
import com.intellij.model.Symbol;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.model.search.CodeReferenceSearcher;
import com.intellij.model.search.LeafOccurrence;
import com.intellij.model.search.SearchRequest;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.psi.HsSymbolReference;
import polyfauna.intellihask.psi.symbol.HsSymbol;

import java.util.Collection;
import java.util.List;

public class HsCodeRefSearcher implements CodeReferenceSearcher{
	
	public @NotNull Language getReferencingLanguage(@NotNull Symbol target){
		return HaskellLanguage.INSTANCE;
	}
	
	public @Nullable SearchRequest getSearchRequest(@NotNull Project project, @NotNull Symbol target){
		if(target instanceof HsSymbol hs)
			return SearchRequest.of(hs.name(), hs.getMaximalSearchScope());
		return null;
	}
	
	public @NotNull Collection<? extends @NotNull PsiSymbolReference> getReferences(@NotNull Symbol target, @NotNull LeafOccurrence occurrence){
		if(target instanceof HsSymbol hs){
			HsSymbolReference elem = PsiTreeUtil.getParentOfType(occurrence.getStart(), false, hs.psiType());
			if(elem != null && elem.resolvesTo(target))
				return List.of(elem);
		}
		return List.of();
	}
}