package polyfauna.intellihask.language.decls;

public enum Locality{
	LOCAL,   // where/let bound bindings
	PRIVATE, // top-level non-exported bindings
	EXPORTED // exported bindings
}