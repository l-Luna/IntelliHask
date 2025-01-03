package polyfauna.intellihask.language.extensions;

// from https://downloads.haskell.org/ghc/latest/docs/users_guide/exts/table.html
// all 130 of them!
public enum Extension{
	AllowAmbiguousTypes,
	ApplicativeDo,
	Arrows,
	BangPatterns,
	BinaryLiterals,
	BlockArguments,
	CApiFFI,
	ConstrainedClassMethods,
	ConstraintKinds,
	CPP,
	CUSKs,
	DataKinds,
	DatatypeContexts,
	DeepSubsumption,
	DefaultSignatures,
	DeriveAnyClass,
	DeriveDataTypeable,
	DeriveFoldable,
	DeriveFunctor,
	DeriveGeneric,
	DeriveLift,
	DeriveTraversable,
	DerivingStrategies,
	DerivingVia,
	DisambiguateRecordFields,
	DuplicateRecordFields,
	EmptyCase,
	EmptyDataDecls,
	EmptyDataDeriving,
	ExistentialQuantification,
	ExplicitForAll,
	ExplicitNamespaces,
	ExtendedDefaultRules,
	ExtendedLiterals,
	FieldSelectors,
	FlexibleContexts,
	FlexibleInstances,
	ForeignFunctionInterface,
	FunctionalDependencies,
	GADTs,
	GADTSyntax,
	GeneralisedNewtypeDeriving,
	GHC2021,
	GHC2024,
	GHCForeignImportPrim,
	Haskell2010,
	Haskell98,
	HexFloatLiterals,
	ImplicitParams,
	ImplicitPrelude,
	ImportQualifiedPost,
	ImpredicativeTypes,
	IncoherentInstances,
	InstanceSigs,
	InterruptibleFFI,
	KindSignatures,
	LambdaCase,
	LexicalNegation,
	LiberalTypeSynonyms,
	LinearTypes,
	ListTuplePuns,
	MagicHash,
	MonadComprehensions,
	MonoLocalBinds,
	MonomorphismRestriction,
	MultilineStrings,
	MultiParamTypeClasses,
	MultiWayIf,
	NamedDefaults,
	NamedFieldPuns,
	NamedWildCards,
	NegativeLiterals,
	NondecreasingIndentation,
	NPlusKPatterns,
	NullaryTypeClasses,
	NumDecimals,
	NumericUnderscores,
	OrPatterns,
	OverlappingInstances,
	OverloadedLabels,
	OverloadedLists,
	OverloadedRecordDot,
	OverloadedRecordUpdate,
	OverloadedStrings,
	PackageImports,
	ParallelListComp,
	PartialTypeSignatures,
	PatternGuards,
	PatternSynonyms,
	PolyKinds,
	PostfixOperators,
	QualifiedDo,
	QuantifiedConstraints,
	QuasiQuotes,
	Rank2Types,
	RankNTypes,
	RebindableSyntax,
	RecordWildCards,
	RecursiveDo,
	RequiredTypeArguments,
	RoleAnnotations,
	Safe,
	ScopedTypeVariables,
	StandaloneDeriving,
	StandaloneKindSignatures,
	StarIsType,
	StaticPointers,
	Strict,
	StrictData,
	TemplateHaskell,
	TemplateHaskellQuotes,
	TraditionalRecordSyntax,
	TransformListComp,
	Trustworthy,
	TupleSections,
	TypeAbstractions,
	TypeApplications,
	TypeData,
	TypeFamilies,
	TypeFamilyDependencies,
	TypeInType,
	TypeOperators,
	TypeSynonymInstances,
	UnboxedSums,
	UnboxedTuples,
	UndecidableInstances,
	UndecidableSuperClasses,
	UnicodeSyntax,
	UnliftedDatatypes,
	UnliftedFFITypes,
	UnliftedNewtypes,
	Unsafe,
	ViewPatterns;
	
	public Toggle on(){
		return new Toggle(this, true);
	}
	
	public Toggle off(){
		return new Toggle(this, false);
	}
}