package polyfauna.intellihask.psi.symbol;

import polyfauna.intellihask.language.type.Type;

import java.util.Optional;

public interface BindingSymbol extends HsSymbol{

	// note: maximal search scope depends on whether the symbol is local (`where`/`let`-bound), non-exported, or exported
	
	// to be run in a read action
	// TODO: distinguish between "literal type", "quick type", and "inferred type"?
	Optional<Type> type();
}