package polyfauna.intellihask.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static polyfauna.intellihask.language.Extension.*;

public class Extensions{
	
	public static final Map<String, Extension> EXTENSIONS = new HashMap<>();
	
	public static final Set<Extension> DEPRECATED_EXTENSIONS = Set.of(
			CUSKs,
			DatatypeContexts,
			GHCForeignImportPrim, // internal-only
			NullaryTypeClasses, // replaced by MultiParamTypeClasses
			Rank2Types, // not *technically* deprecated, but users should use RankNTypes
			TypeInType,
			IncoherentInstances, // replaced by OVERLAPPABLE, OVERLAPPING, and INCOHERENT pragmas
			OverlappingInstances
	);
	
	public static final Map<Extension, List<Extension>> IMPLIED_BY = new HashMap<>();
	
	static{
		for(Extension value : values())
			EXTENSIONS.put(value.name(), value);
		
		IMPLIED_BY.put(ConstrainedClassMethods, List.of(MultiParamTypeClasses, FunctionalDependencies));
		IMPLIED_BY.put(MultiParamTypeClasses, List.of(FunctionalDependencies));
		IMPLIED_BY.put(DeriveFoldable, List.of(DeriveTraversable));
		IMPLIED_BY.put(DeriveFunctor, List.of(DeriveTraversable));
		IMPLIED_BY.put(DerivingStrategies, List.of(DerivingVia));
		IMPLIED_BY.put(DisambiguateRecordFields, List.of(RecordWildCards, DuplicateRecordFields));
		IMPLIED_BY.put(ExplicitForAll, List.of(ScopedTypeVariables, LiberalTypeSynonyms, Rank2Types, RankNTypes, ExistentialQuantification));
		IMPLIED_BY.put(TypeSynonymInstances, List.of(FlexibleInstances));
		IMPLIED_BY.put(GADTSyntax, List.of(GADTs));
		IMPLIED_BY.put(MonoLocalBinds, List.of(GADTs, TypeFamilies, LinearTypes));
		IMPLIED_BY.put(RankNTypes, List.of(ImpredicativeTypes));
		IMPLIED_BY.put(OverlappingInstances, List.of(IncoherentInstances));
		IMPLIED_BY.put(KindSignatures, List.of(TypeFamilies, PolyKinds));
		IMPLIED_BY.put(TypeFamilies, List.of(TypeFamilyDependencies));
		IMPLIED_BY.put(ExplicitNamespaces, List.of(TypeFamilies, TypeOperators));
	}
	
	// TODO:
	//   RebindableSyntax -> *No*ImplicitPrelude
	//   Haskell98, Haskell2010, GHC2021, GHC2024 (which are just very large IMPLIED_BYs)
}