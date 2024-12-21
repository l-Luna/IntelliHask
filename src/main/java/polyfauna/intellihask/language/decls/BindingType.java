package polyfauna.intellihask.language.decls;

public enum BindingType{
	GROUP,      // binding groups; mutually recursive, non-uniquely defined
	PATTERN,    // patterns; isolated, uniquely defined
	FIELD       // record fields; isolated, no actual body, uniquely defined modulo extensions
}