package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.find.usages.api.PsiUsage;
import com.intellij.model.search.SearchService;
import com.intellij.refactoring.rename.api.*;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.psi.symbol.HsSymbol;

public class HsRenameUsageSearcher implements RenameUsageSearcher{
	
	public @Nullable Query<? extends RenameUsage> collectSearchRequest(@NotNull RenameUsageSearchParameters parameters){
		RenameTarget target = parameters.getTarget();
		if(target instanceof HsSymbol hs){
			return SearchService.getInstance()
					.searchPsiSymbolReferences(parameters.getProject(), hs, parameters.getSearchScope())
					.mapping(x -> PsiModifiableRenameUsage.defaultPsiModifiableRenameUsage(PsiUsage.textUsage(x)));
		}
		return null;
	}
}