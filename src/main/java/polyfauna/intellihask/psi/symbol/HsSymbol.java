package polyfauna.intellihask.psi.symbol;

import com.intellij.model.Symbol;
import com.intellij.psi.search.SearchScope;

public interface HsSymbol extends Symbol{

	// short name, used to find references to this symbol
	String name();
	
	// matches the name in SearchTarget
	SearchScope getMaximalSearchScope();
}