package polyfauna.intellihask.language.modules;

public enum Locality{
	LOCAL,   // where/let bound bindings
	PRIVATE, // top-level non-exported bindings
	EXPORTED // exported bindings
}