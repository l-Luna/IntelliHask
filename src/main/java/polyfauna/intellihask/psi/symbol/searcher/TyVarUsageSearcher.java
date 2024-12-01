package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.find.usages.api.*;
import com.intellij.model.search.SearchService;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.psi.symbol.TyVarSymbol;
import polyfauna.intellihask.psi.type.HsTyVar;

public class TyVarUsageSearcher implements UsageSearcher{
	
	public @Nullable Query<? extends Usage> collectSearchRequest(@NotNull UsageSearchParameters parameters){
		SearchTarget target = parameters.getTarget();
		if(target instanceof TyVarSymbol tv){
			return SearchService.getInstance()
					.searchPsiSymbolReferences(parameters.getProject(), tv, parameters.getSearchScope())
					.mapping(PsiUsage::textUsage);
		}
		return null;
	}
}