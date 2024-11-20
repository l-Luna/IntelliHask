package polyfauna.intellihask.language.extensions;

import polyfauna.intellihask.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static polyfauna.intellihask.language.extensions.Extension.*;

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
	
	public static final Set<Extension> DEFAULT_EXTENSIONS = Set.of(
			ImplicitPrelude
	);
	
	public static final Map<Toggle, List<Extension>> IMPLIED_BY = new HashMap<>();
	public static final Map<Extension, List<Toggle>> IMPLIES = new HashMap<>();
	
	static{
		for(Extension value : values())
			EXTENSIONS.put(value.name(), value);
		
		IMPLIED_BY.put(ConstrainedClassMethods.on(), List.of(MultiParamTypeClasses, FunctionalDependencies));
		IMPLIED_BY.put(MultiParamTypeClasses.on(), List.of(FunctionalDependencies));
		IMPLIED_BY.put(DeriveFoldable.on(), List.of(DeriveTraversable));
		IMPLIED_BY.put(DeriveFunctor.on(), List.of(DeriveTraversable));
		IMPLIED_BY.put(DerivingStrategies.on(), List.of(DerivingVia));
		IMPLIED_BY.put(DisambiguateRecordFields.on(), List.of(RecordWildCards, DuplicateRecordFields));
		IMPLIED_BY.put(ExplicitForAll.on(), List.of(ScopedTypeVariables, LiberalTypeSynonyms, Rank2Types, RankNTypes, ExistentialQuantification));
		IMPLIED_BY.put(TypeSynonymInstances.on(), List.of(FlexibleInstances));
		IMPLIED_BY.put(GADTSyntax.on(), List.of(GADTs));
		IMPLIED_BY.put(MonoLocalBinds.on(), List.of(GADTs, TypeFamilies, LinearTypes));
		IMPLIED_BY.put(RankNTypes.on(), List.of(ImpredicativeTypes));
		IMPLIED_BY.put(OverlappingInstances.on(), List.of(IncoherentInstances));
		IMPLIED_BY.put(KindSignatures.on(), List.of(TypeFamilies, PolyKinds));
		IMPLIED_BY.put(TypeFamilies.on(), List.of(TypeFamilyDependencies));
		IMPLIED_BY.put(ExplicitNamespaces.on(), List.of(TypeFamilies, TypeOperators));
		
		IMPLIED_BY.put(ImplicitPrelude.off(), List.of(RebindableSyntax));
		
		IMPLIED_BY.forEach((toggle, causes) -> {
			for(Extension cause : causes)
				IMPLIES.merge(cause, Util.mutListOf(toggle), Util::mergeLLeft);
		});
	}
	
	// TODO:
	//   Haskell98, Haskell2010, GHC2021, GHC2024 (which are just very large implications)
}