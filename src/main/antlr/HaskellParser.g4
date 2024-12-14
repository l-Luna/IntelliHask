parser grammar HaskellParser;

options {
    tokenVocab = HaskellLexer;
}

tokens {
	VOCURLY,
	VCCURLY,
	SEMI
}

// aaa

module
	: OCURLY? semi* pragma* (module_header body module_footer | body) CCURLY? semi* EOF
	;

pragma
    : language_pragma
    | options_ghc
    | simple_options
    ;

language_pragma
    : '{-#' LANGUAGE extension_ (',' extension_)* '#-}' semi?
    ;

options_ghc
    : '{-#' OPTIONS_GHC ('-' (varid | conid))* '#-}' semi?
    ;

simple_options
    : '{-#' OPTIONS ('-' (varid | conid))* '#-}' semi?
    ;

extension_
    : CONID
    ;

module_header
	: MODULE modid exports? WHERE open_
	;

module_footer
	: semi* close semi*
	;

body
    : (impdecl (semi+ impdecl)*)? semi+ (topdecl (semi+ topdecl)*)?
    | (impdecl (semi+ impdecl)*)?
    | (topdecl (semi+ topdecl)*)?
    ;

exports
    : '(' (export (',' export)*)? ','? ')'
    ;

export
    : qvar
    | ( qtycon ( ('(' '..' ')') | ('(' (cname (',' cname)*)? ')'))?)
    | ( qtycls ( ('(' '..' ')') | ('(' (qvar (',' qvar)*)? ')'))?)
    | ( MODULE modid )
    ;

impdecl
    : IMPORT QUALIFIED? modid (AS modid)? impspec?
    ;

impspec
    : ('(' (import_ (',' import_)* ','?)? ')')
    | ( HIDING '(' (import_ (',' import_)* ','?)? ')')
    ;

import_
    : var
    | ( tycon ( ('(' '..' ')') | ('(' (cname (',' cname)*)? ')'))?)
    | ( tycls ( ('(' '..' ')') | ('(' (var (',' var)*)? ')'))?)
    ;

cname
    : var
    | con
    ;

// top-level declarations
topdecl
	: typealiasdecl
	| datadecl
	| newtypedecl
	| classdecl
	| instancedecl
	| defaultdecl
	// inc. generic declarations (see decl; inlined to make PSI traversal nicer)
	| vartypedecl
	| fixitydecl
	| patfundecl
	;

typealiasdecl: TYPE simpletype Eq type;
datadecl: DATA (context '=>')? simpletype '=' constrs deriving?;
newtypedecl: NEWTYPE (context '=>')? simpletype '=' newconstr deriving?;
classdecl: CLASS (scontext '=>')? tycls tyvar (WHERE cdecls)?;
instancedecl: INSTANCE (scontext '=>')? qtycls inst (WHERE idecls)?;
defaultdecl: DEFAULT '(' (type ('*' type)*)? ')';

decls
	: open_ (decl (semi decl?)*)? close
	;

decl
	: vartypedecl
	| fixitydecl
	| patfundecl
	;

cdecls
	: open_ (cdecl (semi cdecl?)*)? close
	;

cdecl
	: vartypedecl
	| fixitydecl
	| varfundecl
	;

idecls
	: open_ (idecl (semi idecl?)*)? close
	;

idecl
	: varfundecl
	;

vartypedecl
	: vars '::' (context '=>')? type
	;

fixitydecl
	: fixity integer? ops
	;

patfundecl
	: (funlhs | pat0) rhs
	;

varfundecl
	: (funlhs | var) rhs
	;

ops
	: op (',' op)*
	;

vars
	: var (',' var)*
	;

fixity
	: INFIXL | INFIXR | INFIX
	;

//---

type
    : btype
    | btype '->' type
    ;

btype
    : atype+
    ;

atype
    : gtycon
    | tyvar
    | ('(' type ')')
    | ('(' type ',' type (',' type)*')')
    | ('[' type ']')
    ;

gtycon
	: qtycon
	| '(' ')'
	| '[' ']'
	| '(' '->' ')'
	| '(' ','+ ')'
	;

//

context
	: class
	| '(' (class (',' class)*)? ')'
	;

class
	: qtycls tyvar
	| qtycls '(' tyvar atype+ ')'
	;

scontext
	: simpleclass
	| '(' (simpleclass (',' simpleclass)*)? ')'
	;

simpleclass
	: qtycls tyvar
	;

simpletype
	: gtycon tyvar*
	;

constrs
	: constr ('|' constr)*
	;

constr
	: con ('!'? atype)*
	| (btype | '!' atype) conop (btype | '!' atype)
	| con '{' (fielddecl (',' fielddecl)*)? '}'
	;

newconstr
	: con atype
	| con '{' var '::' type '}'
	;

fielddecl
	: vars '::' (type | '!' atype)
	;

deriving
	: DERIVING (dclass | '(' (dclass (',' dclass)*)? ')')
	;

dclass: qtycls;

inst
	: gtycon
	| '(' gtycon tyvar* ')'
	| '(' tyvar ',' tyvar (',' tyvar)* ')'
	| '[' tyvar ']'
	| '(' tyvar '->' tyvar ')'
	;

funlhs
	: var apat+
	| pat varop pat
	| '(' funlhs ')' apat+
	;

rhs
	: '=' exp (WHERE decls)?
	| guardrhs (WHERE decls)?
	;

guardrhs
	: guard '=' exp guardrhs?
	;

guard: '|' exp;

// and this is where it gets weird
exp
	: exp0 ('::' (context '=>')? type)?
	;

exp0
	: exp10 (qop exp0)?
	| '-' exp0
	;

exp10
	: '\\' apat+ '->' exp
	| LET decls IN exp
	| IF exp THEN exp ELSE exp
	| CASE exp OF open_ alts close
	| DO open_ stmts close
	| fexp
	;

fexp: upexp+;

upexp
	: aexp upd?
	| qcon upd
	;

upd
	: '{' (fbind (',' fbind)*)? '}'
	;

aexp
	: qvar
	| gcon
	| literal
	| '(' exp ')'
	| '(' exp ',' exp (',' exp)* ')'
	| '[' exp (',' exp)* ']'
	| '[' exp (',' exp)? '..' exp? ']'
	| '[' exp '|' qual (',' qual)* ']'
	| '(' exp qop ')'
	| '(' qop /* TODO: no minus */ exp ')'
	;

qual
	: pat '<-' exp
	| LET decls
	| exp
	;

alts
	: (alt semi+ | NEWLINE)*
	;

alt
	: pat '->' exp (WHERE decls)?
	| pat gdpat (WHERE decls)?
	// |
	;

gdpat
	: guard '->' exp gdpat?
	;

stmts
	: stmt* exp semi?
	;

stmt
	: exp semi
	| pat '<-' exp semi
	| LET decls semi
	| semi
	;

fbind
	: qvar '=' exp
	;

pat: pat0; // no n+k patterns sorry

pat0
	: pat10 (qconop pat0)*
	| '-' pat0
	;

pat10
	: apat
	| gcon apat+
	;

apat
	: var ('@' apat)?
	| gcon
	| qcon '{' (fpat (',' fpat)*)? '}'
	| literal
	| '_'
	| '(' pat ')'
	| '(' pat ',' pat (',' pat)*')'
	| '[' pat (',' pat)* ']'
	| '~' apat
	;

fpat
	: qvar '=' pat
	;

// Variables

gcon
	: '(' ')'
	| '[' ']'
	| '(' ','+ ')'
	| qcon
	;

var: varid | '(' varsym ')';
qvar: qvarid | '(' qvarsym ')';
con: conid | '(' consym ')';
qcon: qconid | '(' gconsym ')';
varop: varsym | '`' varid '`';
qvarop: qvarsym | '`' qvarid '`' ;
conop: consym | '`' conid '`';
qconop: gconsym | '`' qconid '`';
op: varop | conop;
qop: qvarop | qconop;
gconsym: ':' | qconsym ;

tyvar: varid;
tycon: conid;
tycls: conid;
modid: conid ('.' conid)*;
qvarid: (modid '.')? varid;
qconid: (modid '.')? conid;
qtycon: (modid '.')? tycon;
qtycls: (modid '.')? tycls;
qvarsym: (modid '.')? varsym;
qconsym: (modid '.')? consym;
consym: ':' ascSymbol*;

varid: (VARID | special_id);
conid: CONID;

// not poking at these until i Get It
qvarsym_no_minus
    : varsym_no_minus
    | qvarsym
    ;

varsym
    : varsym_no_minus
    | '-'
    ;

varsym_no_minus
    : ascSymbol+
    ;

// These special_ids are treated as keywords in various places,
// but as ordinary ids elsewhere.   'special_id' collects all these
// except 'unsafe', 'interruptible', 'forall', 'family', 'role', 'stock', and
// 'anyclass', whose treatment differs depending on context
special_id
    : 'as'
    | 'qualified'
    | 'hiding'
    | 'export'
    | 'stdcall'
    | 'ccall'
    | 'capi'
    | 'javascript'
    | 'stock'
    | 'anyclass'
    | 'via'
    ;

// Layout

open_
    : VOCURLY
    | OCURLY
    ;

close
    : VCCURLY
    | CCURLY
    ;

semi
	: NEWLINE | ';' | SEMI
	;

// -------------------------------------------

literal
	: integer
	| pfloat
	| pchar
	| pstring
	;

special
    : '('
    | ')'
    | ','
    | ';'
    | '['
    | ']'
    | '`'
    | '{'
    | '}'
    ;

symbol
    : ascSymbol
    ;

ascSymbol
    : '!'
    | '#'
    | '$'
    | '%'
    | '&'
    | '*'
    | '+'
    | '.'
    | '/'
    | '<'
    | '='
    | '>'
    | '?'
    | '@'
    | '\\'
    | '^'
    | '|'
    | '~'
    | ':'
    ;

integer
    : DECIMAL
    | OCTAL
    | HEXADECIMAL
    ;

pfloat
    : FLOAT
    ;

pchar
    : CHAR
    ;

pstring
    : STRING
    ;