/*
BSD License
Copyright (c) 2020, Evgeniy Slobodkin
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of Tom Everett nor the names of its contributors
   may be used to endorse or promote products derived from this software
   without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// luna notes:
// we can't make synthetic tokens for indentation, you can't annotate tokens with indentations code-side, and complex state slows things down
// instead we handle indentation on the IJ-parser-side, synthesizing tokens on-the-spot
// skip is also disallowed, so we use our friendly neighborhood `HIDDEN` channel instead; we need that whitespace anyways^

lexer grammar HaskellLexer;

tokens {
	VOCURLY,
	VCCURLY,
	SEMI
}

channels {
	WSC
}

NEWLINE: ('\r'? '\n' | '\r') -> channel(WSC);

TAB: [\t]+ -> channel(WSC);

WS: [\u0020\u00a0\u1680\u2000\u200a\u202f\u205f\u3000]+ -> channel(WSC);

AS        : 'as';
CASE      : 'case';
CLASS     : 'class';
DATA      : 'data';
DEFAULT   : 'default';
DERIVING  : 'deriving';
DO        : 'do';
ELSE      : 'else';
HIDING    : 'hiding';
IF        : 'if';
IMPORT    : 'import';
IN        : 'in';
INFIX     : 'infix';
INFIXL    : 'infixl';
INFIXR    : 'infixr';
INSTANCE  : 'instance';
LET       : 'let';
MODULE    : 'module';
NEWTYPE   : 'newtype';
OF        : 'of';
QUALIFIED : 'qualified';
THEN      : 'then';
TYPE      : 'type';
WHERE     : 'where';
WILDCARD  : '_';

FORALL        : 'forall';
FOREIGN       : 'foreign';
EXPORT        : 'export';
SAFE          : 'safe';
INTERRUPTIBLE : 'interruptible';
UNSAFE        : 'unsafe';
MDO           : 'mdo';
FAMILY        : 'family';
ROLE          : 'role';
STDCALL       : 'stdcall';
CCALL         : 'ccall';
CAPI          : 'capi';
CPPCALL       : 'cplusplus';
JSCALL        : 'javascript';
REC           : 'rec';
GROUP         : 'group';
BY            : 'by';
USING         : 'using';
PATTERN       : 'pattern';
STOCK         : 'stock';
ANYCLASS      : 'anyclass';
VIA           : 'via';

LANGUAGE     : 'LANGUAGE';
OPTIONS_GHC  : 'OPTIONS_GHC';
OPTIONS      : 'OPTIONS';
INLINE       : 'INLINE';
NOINLINE     : 'NOINLINE';
SPECIALISE   : 'SPECIALISE';
SPECINLINE   : 'SPECIALISE_INLINE';
SOURCE       : 'SOURCE';
RULES        : 'RULES';
SCC          : 'SCC';
DEPRECATED   : 'DEPRECATED';
WARNING      : 'WARNING';
UNPACK       : 'UNPACK';
NOUNPACK     : 'NOUNPACK';
ANN          : 'ANN';
MINIMAL      : 'MINIMAL';
CTYPE        : 'CTYPE';
OVERLAPPING  : 'OVERLAPPING';
OVERLAPPABLE : 'OVERLAPPABLE';
OVERLAPS     : 'OVERLAPS';
INCOHERENT   : 'INCOHERENT';
COMPLETE     : 'COMPLETE';

LCASE: '\\' (NEWLINE | WS)* 'case';

fragment SYMBOL    : ASCSYMBOL | UNISYMBOL;
DoubleArrow        : '=>';
DoubleColon        : '::';
Arrow              : '->';
Revarrow           : '<-';
LarrowTail         : '-<';
RarrowTail         : '>-';
LLarrowTail        : '-<<';
RRarrowTail        : '>>-';
Hash               : '#';
Less               : '<';
Greater            : '>';
Ampersand          : '&';
Pipe               : '|';
Bang               : '!';
Caret              : '^';
Plus               : '+';
Minus              : '-';
Asterisk           : '*';
Percent            : '%';
Divide             : '/';
Tilde              : '~';
Atsign             : '@';
DDollar            : '$$';
Dollar             : '$';
DoubleDot          : '..';
Dot                : '.';
Semi               : ';';
QuestionMark       : '?';
Comma              : ',';
Colon              : ':';
Eq                 : '=';
Quote              : '\'';
DoubleQuote        : '\'\'';
SQuote             : '"';
ReverseSlash       : '\\';
BackQuote          : '`';
AopenParen         : '(|' WS;
AcloseParen        : WS '|)';
TopenTexpQuote     : '[||';
TcloseTExpQoute    : '||]';
TopenExpQuote      : '[|';
TopenPatQuote      : '[p|';
TopenTypQoute      : '[t|';
TopenDecQoute      : '[d|';
TcloseQoute        : '|]';
OpenBoxParen       : '(#';
CloseBoxParen      : '#)';
OpenRoundBracket   : '(';
CloseRoundBracket  : ')';
OpenSquareBracket  : '[';
CloseSquareBracket : ']';

CHAR:
    '\'' (
        ' '
        | DECIMAL
        | SMALL
        | LARGE
        | SYMBOL
        | DIGIT
        | ','
        | ';'
        | '('
        | ')'
        | '['
        | ']'
        | '`'
        | '"'
        | '\\0'
        | '\\n'
        | '\\"'
        | '\\\''
        | '\\\\'
    ) '\''
;

STRING:
    '"' (
        ' '
        | DECIMAL
        | SMALL
        | LARGE
        | SYMBOL
        | DIGIT
        | ','
        | ';'
        | '('
        | ')'
        | '['
        | ']'
        | '{'
        | '}'
        | '`'
        | '\''
        | '\\"'
    )* '"'
;

VARID : SMALL (SMALL | LARGE | DIGIT | '\'')* '#'*;
CONID : LARGE (SMALL | LARGE | DIGIT | '\'')* '#'*;

OpenPragmaBracket  : '{-#';
ClosePragmaBracket : '#-}';

// For CPP extension
// read about
// https://medium.com/swiftify/parsing-preprocessor-directives-in-objective-c-714a3dde570
// directives are skip now
// MultiLineMacro : '#' (~ [\n]*? '\\' '\r'? '\n')+ ~ [\n]+ -> skip;
// Directive : '#' ~ [\n]* -> skip;

COMMENT  : '--' (~[\r\n])* -> channel(HIDDEN);
// explicitly handle the case of an empty block comment; otherwise, it fails to find not-hash and parses wrong
// (the outer brackets just allow for the `-> channel` command)
NCOMMENT : ('{--}' | ('{-' ~[#] .*? '-}')) -> channel(HIDDEN);

OCURLY  : '{';
CCURLY  : '}';

DECIMAL     : DIGIT+;
OCTAL       : '0' [oO] OCTIT+;
HEXADECIMAL : '0' [xX] HEXIT+;

fragment DIGIT: ASCDIGIT | UNIDIGIT;

fragment ASCDIGIT: [0-9];
fragment UNIDIGIT:        // : '\u0030'..'\u0039'       // Basic_Latin
    '\u0660' ..'\u0669'   // Arabic
    | '\u06f0' ..'\u06f9' // Arabic
    | '\u07c0' ..'\u07c9' // NKo
    | '\u0966' ..'\u096f' // Devanagari
    | '\u09e6' ..'\u09ef' // Bengali
    | '\u0a66' ..'\u0a6f' // Gurmukhi
    | '\u0ae6' ..'\u0aef' // Gujarati
    | '\u0b66' ..'\u0b6f' // Oriya
    | '\u0be6' ..'\u0bef' // Tamil
    | '\u0c66' ..'\u0c6f' // Telugu
    | '\u0ce6' ..'\u0cef' // Kannada
    | '\u0d66' ..'\u0d6f' // Malayalam
    | '\u0de6' ..'\u0def' // Sinhala
    | '\u0e50' ..'\u0e59' // Thai
    | '\u0ed0' ..'\u0ed9' // Lao
    | '\u0f20' ..'\u0f29' // Tibetan
    | '\u1040' ..'\u1049' // Myanmar
    | '\u1090' ..'\u1099' // Myanmar
    | '\u17e0' ..'\u17e9' // Khmer
    | '\u1810' ..'\u1819' // Mongolian
    | '\u1946' ..'\u194f' // Limbu
    | '\u19d0' ..'\u19d9' // New_Tai_Lue
    | '\u1a80' ..'\u1a89' // Tai_Tham
    | '\u1a90' ..'\u1a99' // Tai_Tham
    | '\u1b50' ..'\u1b59' // Balinese
    | '\u1bb0' ..'\u1bb9' // Sundanese
    | '\u1c40' ..'\u1c49' // Lepcha
    | '\u1c50' ..'\u1c59' // Ol_Chiki
    | '\ua620' ..'\ua629' // Vai
    | '\ua8d0' ..'\ua8d9' // Saurashtra
    | '\ua900' ..'\ua909' // Kayah_Li
    | '\ua9d0' ..'\ua9d9' // Javanese
    | '\ua9f0' ..'\ua9f9' // Myanmar_Extended-B
    | '\uaa50' ..'\uaa59' // Cham
    | '\uabf0' ..'\uabf9' // Meetei_Mayek
    | '\uff10' ..'\uff19' // Halfwidth_and_Fullwidth_Forms
;

fragment OCTIT : [0-7];
fragment HEXIT : [0-9] | [A-F] | [a-f];

FLOAT    : (DECIMAL '.' DECIMAL (EXPONENT)?) | (DECIMAL EXPONENT);
EXPONENT : [eE] [+-]? DECIMAL;

fragment LARGE: ASCLARGE | UNILARGE;

fragment ASCLARGE: [A-Z];
fragment UNILARGE:        // : '\u0041'..'\u005a'       // Basic_Latin
    '\u00c0' ..'\u00d6'   // Latin-1_Supplement
    | '\u00d8' ..'\u00de' // Latin-1_Supplement
    | '\u0100'            // Latin_Extended-A
    | '\u0102'            // Latin_Extended-A
    | '\u0104'            // Latin_Extended-A
    | '\u0106'            // Latin_Extended-A
    | '\u0108'            // Latin_Extended-A
    | '\u010a'            // Latin_Extended-A
    | '\u010c'            // Latin_Extended-A
    | '\u010e'            // Latin_Extended-A
    | '\u0110'            // Latin_Extended-A
    | '\u0112'            // Latin_Extended-A
    | '\u0114'            // Latin_Extended-A
    | '\u0116'            // Latin_Extended-A
    | '\u0118'            // Latin_Extended-A
    | '\u011a'            // Latin_Extended-A
    | '\u011c'            // Latin_Extended-A
    | '\u011e'            // Latin_Extended-A
    | '\u0120'            // Latin_Extended-A
    | '\u0122'            // Latin_Extended-A
    | '\u0124'            // Latin_Extended-A
    | '\u0126'            // Latin_Extended-A
    | '\u0128'            // Latin_Extended-A
    | '\u012a'            // Latin_Extended-A
    | '\u012c'            // Latin_Extended-A
    | '\u012e'            // Latin_Extended-A
    | '\u0130'            // Latin_Extended-A
    | '\u0132'            // Latin_Extended-A
    | '\u0134'            // Latin_Extended-A
    | '\u0136'            // Latin_Extended-A
    | '\u0139'            // Latin_Extended-A
    | '\u013b'            // Latin_Extended-A
    | '\u013d'            // Latin_Extended-A
    | '\u013f'            // Latin_Extended-A
    | '\u0141'            // Latin_Extended-A
    | '\u0143'            // Latin_Extended-A
    | '\u0145'            // Latin_Extended-A
    | '\u0147'            // Latin_Extended-A
    | '\u014a'            // Latin_Extended-A
    | '\u014c'            // Latin_Extended-A
    | '\u014e'            // Latin_Extended-A
    | '\u0150'            // Latin_Extended-A
    | '\u0152'            // Latin_Extended-A
    | '\u0154'            // Latin_Extended-A
    | '\u0156'            // Latin_Extended-A
    | '\u0158'            // Latin_Extended-A
    | '\u015a'            // Latin_Extended-A
    | '\u015c'            // Latin_Extended-A
    | '\u015e'            // Latin_Extended-A
    | '\u0160'            // Latin_Extended-A
    | '\u0162'            // Latin_Extended-A
    | '\u0164'            // Latin_Extended-A
    | '\u0166'            // Latin_Extended-A
    | '\u0168'            // Latin_Extended-A
    | '\u016a'            // Latin_Extended-A
    | '\u016c'            // Latin_Extended-A
    | '\u016e'            // Latin_Extended-A
    | '\u0170'            // Latin_Extended-A
    | '\u0172'            // Latin_Extended-A
    | '\u0174'            // Latin_Extended-A
    | '\u0176'            // Latin_Extended-A
    | '\u0178' ..'\u0179' // Latin_Extended-A
    | '\u017b'            // Latin_Extended-A
    | '\u017d'            // Latin_Extended-A
    | '\u0181' ..'\u0182' // Latin_Extended-B
    | '\u0184'            // Latin_Extended-B
    | '\u0186' ..'\u0187' // Latin_Extended-B
    | '\u0189' ..'\u018b' // Latin_Extended-B
    | '\u018e' ..'\u0191' // Latin_Extended-B
    | '\u0193' ..'\u0194' // Latin_Extended-B
    | '\u0196' ..'\u0198' // Latin_Extended-B
    | '\u019c' ..'\u019d' // Latin_Extended-B
    | '\u019f' ..'\u01a0' // Latin_Extended-B
    | '\u01a2'            // Latin_Extended-B
    | '\u01a4'            // Latin_Extended-B
    | '\u01a6' ..'\u01a7' // Latin_Extended-B
    | '\u01a9'            // Latin_Extended-B
    | '\u01ac'            // Latin_Extended-B
    | '\u01ae' ..'\u01af' // Latin_Extended-B
    | '\u01b1' ..'\u01b3' // Latin_Extended-B
    | '\u01b5'            // Latin_Extended-B
    | '\u01b7' ..'\u01b8' // Latin_Extended-B
    | '\u01bc'            // Latin_Extended-B
    | '\u01c4'            // Latin_Extended-B
    | '\u01c7'            // Latin_Extended-B
    | '\u01ca'            // Latin_Extended-B
    | '\u01cd'            // Latin_Extended-B
    | '\u01cf'            // Latin_Extended-B
    | '\u01d1'            // Latin_Extended-B
    | '\u01d3'            // Latin_Extended-B
    | '\u01d5'            // Latin_Extended-B
    | '\u01d7'            // Latin_Extended-B
    | '\u01d9'            // Latin_Extended-B
    | '\u01db'            // Latin_Extended-B
    | '\u01de'            // Latin_Extended-B
    | '\u01e0'            // Latin_Extended-B
    | '\u01e2'            // Latin_Extended-B
    | '\u01e4'            // Latin_Extended-B
    | '\u01e6'            // Latin_Extended-B
    | '\u01e8'            // Latin_Extended-B
    | '\u01ea'            // Latin_Extended-B
    | '\u01ec'            // Latin_Extended-B
    | '\u01ee'            // Latin_Extended-B
    | '\u01f1'            // Latin_Extended-B
    | '\u01f4'            // Latin_Extended-B
    | '\u01f6' ..'\u01f8' // Latin_Extended-B
    | '\u01fa'            // Latin_Extended-B
    | '\u01fc'            // Latin_Extended-B
    | '\u01fe'            // Latin_Extended-B
    | '\u0200'            // Latin_Extended-B
    | '\u0202'            // Latin_Extended-B
    | '\u0204'            // Latin_Extended-B
    | '\u0206'            // Latin_Extended-B
    | '\u0208'            // Latin_Extended-B
    | '\u020a'            // Latin_Extended-B
    | '\u020c'            // Latin_Extended-B
    | '\u020e'            // Latin_Extended-B
    | '\u0210'            // Latin_Extended-B
    | '\u0212'            // Latin_Extended-B
    | '\u0214'            // Latin_Extended-B
    | '\u0216'            // Latin_Extended-B
    | '\u0218'            // Latin_Extended-B
    | '\u021a'            // Latin_Extended-B
    | '\u021c'            // Latin_Extended-B
    | '\u021e'            // Latin_Extended-B
    | '\u0220'            // Latin_Extended-B
    | '\u0222'            // Latin_Extended-B
    | '\u0224'            // Latin_Extended-B
    | '\u0226'            // Latin_Extended-B
    | '\u0228'            // Latin_Extended-B
    | '\u022a'            // Latin_Extended-B
    | '\u022c'            // Latin_Extended-B
    | '\u022e'            // Latin_Extended-B
    | '\u0230'            // Latin_Extended-B
    | '\u0232'            // Latin_Extended-B
    | '\u023a' ..'\u023b' // Latin_Extended-B
    | '\u023d' ..'\u023e' // Latin_Extended-B
    | '\u0241'            // Latin_Extended-B
    | '\u0243' ..'\u0246' // Latin_Extended-B
    | '\u0248'            // Latin_Extended-B
    | '\u024a'            // Latin_Extended-B
    | '\u024c'            // Latin_Extended-B
    | '\u024e'            // Latin_Extended-B
    | '\u0370'            // Greek_and_Coptic
    | '\u0372'            // Greek_and_Coptic
    | '\u0376'            // Greek_and_Coptic
    | '\u037f'            // Greek_and_Coptic
    | '\u0386'            // Greek_and_Coptic
    | '\u0388' ..'\u038a' // Greek_and_Coptic
    | '\u038c'            // Greek_and_Coptic
    | '\u038e' ..'\u038f' // Greek_and_Coptic
    | '\u0391' ..'\u03a1' // Greek_and_Coptic
    | '\u03a3' ..'\u03ab' // Greek_and_Coptic
    | '\u03cf'            // Greek_and_Coptic
    | '\u03d2' ..'\u03d4' // Greek_and_Coptic
    | '\u03d8'            // Greek_and_Coptic
    | '\u03da'            // Greek_and_Coptic
    | '\u03dc'            // Greek_and_Coptic
    | '\u03de'            // Greek_and_Coptic
    | '\u03e0'            // Greek_and_Coptic
    | '\u03e2'            // Greek_and_Coptic
    | '\u03e4'            // Greek_and_Coptic
    | '\u03e6'            // Greek_and_Coptic
    | '\u03e8'            // Greek_and_Coptic
    | '\u03ea'            // Greek_and_Coptic
    | '\u03ec'            // Greek_and_Coptic
    | '\u03ee'            // Greek_and_Coptic
    | '\u03f4'            // Greek_and_Coptic
    | '\u03f7'            // Greek_and_Coptic
    | '\u03f9' ..'\u03fa' // Greek_and_Coptic
    | '\u03fd' ..'\u042f' // Greek_and_Coptic
    | '\u0460'            // Cyrillic
    | '\u0462'            // Cyrillic
    | '\u0464'            // Cyrillic
    | '\u0466'            // Cyrillic
    | '\u0468'            // Cyrillic
    | '\u046a'            // Cyrillic
    | '\u046c'            // Cyrillic
    | '\u046e'            // Cyrillic
    | '\u0470'            // Cyrillic
    | '\u0472'            // Cyrillic
    | '\u0474'            // Cyrillic
    | '\u0476'            // Cyrillic
    | '\u0478'            // Cyrillic
    | '\u047a'            // Cyrillic
    | '\u047c'            // Cyrillic
    | '\u047e'            // Cyrillic
    | '\u0480'            // Cyrillic
    | '\u048a'            // Cyrillic
    | '\u048c'            // Cyrillic
    | '\u048e'            // Cyrillic
    | '\u0490'            // Cyrillic
    | '\u0492'            // Cyrillic
    | '\u0494'            // Cyrillic
    | '\u0496'            // Cyrillic
    | '\u0498'            // Cyrillic
    | '\u049a'            // Cyrillic
    | '\u049c'            // Cyrillic
    | '\u049e'            // Cyrillic
    | '\u04a0'            // Cyrillic
    | '\u04a2'            // Cyrillic
    | '\u04a4'            // Cyrillic
    | '\u04a6'            // Cyrillic
    | '\u04a8'            // Cyrillic
    | '\u04aa'            // Cyrillic
    | '\u04ac'            // Cyrillic
    | '\u04ae'            // Cyrillic
    | '\u04b0'            // Cyrillic
    | '\u04b2'            // Cyrillic
    | '\u04b4'            // Cyrillic
    | '\u04b6'            // Cyrillic
    | '\u04b8'            // Cyrillic
    | '\u04ba'            // Cyrillic
    | '\u04bc'            // Cyrillic
    | '\u04be'            // Cyrillic
    | '\u04c0' ..'\u04c1' // Cyrillic
    | '\u04c3'            // Cyrillic
    | '\u04c5'            // Cyrillic
    | '\u04c7'            // Cyrillic
    | '\u04c9'            // Cyrillic
    | '\u04cb'            // Cyrillic
    | '\u04cd'            // Cyrillic
    | '\u04d0'            // Cyrillic
    | '\u04d2'            // Cyrillic
    | '\u04d4'            // Cyrillic
    | '\u04d6'            // Cyrillic
    | '\u04d8'            // Cyrillic
    | '\u04da'            // Cyrillic
    | '\u04dc'            // Cyrillic
    | '\u04de'            // Cyrillic
    | '\u04e0'            // Cyrillic
    | '\u04e2'            // Cyrillic
    | '\u04e4'            // Cyrillic
    | '\u04e6'            // Cyrillic
    | '\u04e8'            // Cyrillic
    | '\u04ea'            // Cyrillic
    | '\u04ec'            // Cyrillic
    | '\u04ee'            // Cyrillic
    | '\u04f0'            // Cyrillic
    | '\u04f2'            // Cyrillic
    | '\u04f4'            // Cyrillic
    | '\u04f6'            // Cyrillic
    | '\u04f8'            // Cyrillic
    | '\u04fa'            // Cyrillic
    | '\u04fc'            // Cyrillic
    | '\u04fe'            // Cyrillic
    | '\u0500'            // Cyrillic_Supplement
    | '\u0502'            // Cyrillic_Supplement
    | '\u0504'            // Cyrillic_Supplement
    | '\u0506'            // Cyrillic_Supplement
    | '\u0508'            // Cyrillic_Supplement
    | '\u050a'            // Cyrillic_Supplement
    | '\u050c'            // Cyrillic_Supplement
    | '\u050e'            // Cyrillic_Supplement
    | '\u0510'            // Cyrillic_Supplement
    | '\u0512'            // Cyrillic_Supplement
    | '\u0514'            // Cyrillic_Supplement
    | '\u0516'            // Cyrillic_Supplement
    | '\u0518'            // Cyrillic_Supplement
    | '\u051a'            // Cyrillic_Supplement
    | '\u051c'            // Cyrillic_Supplement
    | '\u051e'            // Cyrillic_Supplement
    | '\u0520'            // Cyrillic_Supplement
    | '\u0522'            // Cyrillic_Supplement
    | '\u0524'            // Cyrillic_Supplement
    | '\u0526'            // Cyrillic_Supplement
    | '\u0528'            // Cyrillic_Supplement
    | '\u052a'            // Cyrillic_Supplement
    | '\u052c'            // Cyrillic_Supplement
    | '\u052e'            // Cyrillic_Supplement
    | '\u0531' ..'\u0556' // Armenian
    | '\u10a0' ..'\u10c5' // Georgian
    | '\u10c7'            // Georgian
    | '\u10cd'            // Georgian
    | '\u13a0' ..'\u13f5' // Cherokee
    | '\u1e00'            // Latin_Extended_Additional
    | '\u1e02'            // Latin_Extended_Additional
    | '\u1e04'            // Latin_Extended_Additional
    | '\u1e06'            // Latin_Extended_Additional
    | '\u1e08'            // Latin_Extended_Additional
    | '\u1e0a'            // Latin_Extended_Additional
    | '\u1e0c'            // Latin_Extended_Additional
    | '\u1e0e'            // Latin_Extended_Additional
    | '\u1e10'            // Latin_Extended_Additional
    | '\u1e12'            // Latin_Extended_Additional
    | '\u1e14'            // Latin_Extended_Additional
    | '\u1e16'            // Latin_Extended_Additional
    | '\u1e18'            // Latin_Extended_Additional
    | '\u1e1a'            // Latin_Extended_Additional
    | '\u1e1c'            // Latin_Extended_Additional
    | '\u1e1e'            // Latin_Extended_Additional
    | '\u1e20'            // Latin_Extended_Additional
    | '\u1e22'            // Latin_Extended_Additional
    | '\u1e24'            // Latin_Extended_Additional
    | '\u1e26'            // Latin_Extended_Additional
    | '\u1e28'            // Latin_Extended_Additional
    | '\u1e2a'            // Latin_Extended_Additional
    | '\u1e2c'            // Latin_Extended_Additional
    | '\u1e2e'            // Latin_Extended_Additional
    | '\u1e30'            // Latin_Extended_Additional
    | '\u1e32'            // Latin_Extended_Additional
    | '\u1e34'            // Latin_Extended_Additional
    | '\u1e36'            // Latin_Extended_Additional
    | '\u1e38'            // Latin_Extended_Additional
    | '\u1e3a'            // Latin_Extended_Additional
    | '\u1e3c'            // Latin_Extended_Additional
    | '\u1e3e'            // Latin_Extended_Additional
    | '\u1e40'            // Latin_Extended_Additional
    | '\u1e42'            // Latin_Extended_Additional
    | '\u1e44'            // Latin_Extended_Additional
    | '\u1e46'            // Latin_Extended_Additional
    | '\u1e48'            // Latin_Extended_Additional
    | '\u1e4a'            // Latin_Extended_Additional
    | '\u1e4c'            // Latin_Extended_Additional
    | '\u1e4e'            // Latin_Extended_Additional
    | '\u1e50'            // Latin_Extended_Additional
    | '\u1e52'            // Latin_Extended_Additional
    | '\u1e54'            // Latin_Extended_Additional
    | '\u1e56'            // Latin_Extended_Additional
    | '\u1e58'            // Latin_Extended_Additional
    | '\u1e5a'            // Latin_Extended_Additional
    | '\u1e5c'            // Latin_Extended_Additional
    | '\u1e5e'            // Latin_Extended_Additional
    | '\u1e60'            // Latin_Extended_Additional
    | '\u1e62'            // Latin_Extended_Additional
    | '\u1e64'            // Latin_Extended_Additional
    | '\u1e66'            // Latin_Extended_Additional
    | '\u1e68'            // Latin_Extended_Additional
    | '\u1e6a'            // Latin_Extended_Additional
    | '\u1e6c'            // Latin_Extended_Additional
    | '\u1e6e'            // Latin_Extended_Additional
    | '\u1e70'            // Latin_Extended_Additional
    | '\u1e72'            // Latin_Extended_Additional
    | '\u1e74'            // Latin_Extended_Additional
    | '\u1e76'            // Latin_Extended_Additional
    | '\u1e78'            // Latin_Extended_Additional
    | '\u1e7a'            // Latin_Extended_Additional
    | '\u1e7c'            // Latin_Extended_Additional
    | '\u1e7e'            // Latin_Extended_Additional
    | '\u1e80'            // Latin_Extended_Additional
    | '\u1e82'            // Latin_Extended_Additional
    | '\u1e84'            // Latin_Extended_Additional
    | '\u1e86'            // Latin_Extended_Additional
    | '\u1e88'            // Latin_Extended_Additional
    | '\u1e8a'            // Latin_Extended_Additional
    | '\u1e8c'            // Latin_Extended_Additional
    | '\u1e8e'            // Latin_Extended_Additional
    | '\u1e90'            // Latin_Extended_Additional
    | '\u1e92'            // Latin_Extended_Additional
    | '\u1e94'            // Latin_Extended_Additional
    | '\u1e9e'            // Latin_Extended_Additional
    | '\u1ea0'            // Latin_Extended_Additional
    | '\u1ea2'            // Latin_Extended_Additional
    | '\u1ea4'            // Latin_Extended_Additional
    | '\u1ea6'            // Latin_Extended_Additional
    | '\u1ea8'            // Latin_Extended_Additional
    | '\u1eaa'            // Latin_Extended_Additional
    | '\u1eac'            // Latin_Extended_Additional
    | '\u1eae'            // Latin_Extended_Additional
    | '\u1eb0'            // Latin_Extended_Additional
    | '\u1eb2'            // Latin_Extended_Additional
    | '\u1eb4'            // Latin_Extended_Additional
    | '\u1eb6'            // Latin_Extended_Additional
    | '\u1eb8'            // Latin_Extended_Additional
    | '\u1eba'            // Latin_Extended_Additional
    | '\u1ebc'            // Latin_Extended_Additional
    | '\u1ebe'            // Latin_Extended_Additional
    | '\u1ec0'            // Latin_Extended_Additional
    | '\u1ec2'            // Latin_Extended_Additional
    | '\u1ec4'            // Latin_Extended_Additional
    | '\u1ec6'            // Latin_Extended_Additional
    | '\u1ec8'            // Latin_Extended_Additional
    | '\u1eca'            // Latin_Extended_Additional
    | '\u1ecc'            // Latin_Extended_Additional
    | '\u1ece'            // Latin_Extended_Additional
    | '\u1ed0'            // Latin_Extended_Additional
    | '\u1ed2'            // Latin_Extended_Additional
    | '\u1ed4'            // Latin_Extended_Additional
    | '\u1ed6'            // Latin_Extended_Additional
    | '\u1ed8'            // Latin_Extended_Additional
    | '\u1eda'            // Latin_Extended_Additional
    | '\u1edc'            // Latin_Extended_Additional
    | '\u1ede'            // Latin_Extended_Additional
    | '\u1ee0'            // Latin_Extended_Additional
    | '\u1ee2'            // Latin_Extended_Additional
    | '\u1ee4'            // Latin_Extended_Additional
    | '\u1ee6'            // Latin_Extended_Additional
    | '\u1ee8'            // Latin_Extended_Additional
    | '\u1eea'            // Latin_Extended_Additional
    | '\u1eec'            // Latin_Extended_Additional
    | '\u1eee'            // Latin_Extended_Additional
    | '\u1ef0'            // Latin_Extended_Additional
    | '\u1ef2'            // Latin_Extended_Additional
    | '\u1ef4'            // Latin_Extended_Additional
    | '\u1ef6'            // Latin_Extended_Additional
    | '\u1ef8'            // Latin_Extended_Additional
    | '\u1efa'            // Latin_Extended_Additional
    | '\u1efc'            // Latin_Extended_Additional
    | '\u1efe'            // Latin_Extended_Additional
    | '\u1f08' ..'\u1f0f' // Greek_Extended
    | '\u1f18' ..'\u1f1d' // Greek_Extended
    | '\u1f28' ..'\u1f2f' // Greek_Extended
    | '\u1f38' ..'\u1f3f' // Greek_Extended
    | '\u1f48' ..'\u1f4d' // Greek_Extended
    | '\u1f59'            // Greek_Extended
    | '\u1f5b'            // Greek_Extended
    | '\u1f5d'            // Greek_Extended
    | '\u1f5f'            // Greek_Extended
    | '\u1f68' ..'\u1f6f' // Greek_Extended
    | '\u1fb8' ..'\u1fbb' // Greek_Extended
    | '\u1fc8' ..'\u1fcb' // Greek_Extended
    | '\u1fd8' ..'\u1fdb' // Greek_Extended
    | '\u1fe8' ..'\u1fec' // Greek_Extended
    | '\u1ff8' ..'\u1ffb' // Greek_Extended
    | '\u2102'            // Letterlike_Symbols
    | '\u2107'            // Letterlike_Symbols
    | '\u210b' ..'\u210d' // Letterlike_Symbols
    | '\u2110' ..'\u2112' // Letterlike_Symbols
    | '\u2115'            // Letterlike_Symbols
    | '\u2119' ..'\u211d' // Letterlike_Symbols
    | '\u2124'            // Letterlike_Symbols
    | '\u2126'            // Letterlike_Symbols
    | '\u2128'            // Letterlike_Symbols
    | '\u212a' ..'\u212d' // Letterlike_Symbols
    | '\u2130' ..'\u2133' // Letterlike_Symbols
    | '\u213e' ..'\u213f' // Letterlike_Symbols
    | '\u2145'            // Letterlike_Symbols
    | '\u2183'            // Number_Forms
    | '\u2c00' ..'\u2c2e' // Glagolitic
    | '\u2c60'            // Latin_Extended-C
    | '\u2c62' ..'\u2c64' // Latin_Extended-C
    | '\u2c67'            // Latin_Extended-C
    | '\u2c69'            // Latin_Extended-C
    | '\u2c6b'            // Latin_Extended-C
    | '\u2c6d' ..'\u2c70' // Latin_Extended-C
    | '\u2c72'            // Latin_Extended-C
    | '\u2c75'            // Latin_Extended-C
    | '\u2c7e' ..'\u2c80' // Latin_Extended-C
    | '\u2c82'            // Coptic
    | '\u2c84'            // Coptic
    | '\u2c86'            // Coptic
    | '\u2c88'            // Coptic
    | '\u2c8a'            // Coptic
    | '\u2c8c'            // Coptic
    | '\u2c8e'            // Coptic
    | '\u2c90'            // Coptic
    | '\u2c92'            // Coptic
    | '\u2c94'            // Coptic
    | '\u2c96'            // Coptic
    | '\u2c98'            // Coptic
    | '\u2c9a'            // Coptic
    | '\u2c9c'            // Coptic
    | '\u2c9e'            // Coptic
    | '\u2ca0'            // Coptic
    | '\u2ca2'            // Coptic
    | '\u2ca4'            // Coptic
    | '\u2ca6'            // Coptic
    | '\u2ca8'            // Coptic
    | '\u2caa'            // Coptic
    | '\u2cac'            // Coptic
    | '\u2cae'            // Coptic
    | '\u2cb0'            // Coptic
    | '\u2cb2'            // Coptic
    | '\u2cb4'            // Coptic
    | '\u2cb6'            // Coptic
    | '\u2cb8'            // Coptic
    | '\u2cba'            // Coptic
    | '\u2cbc'            // Coptic
    | '\u2cbe'            // Coptic
    | '\u2cc0'            // Coptic
    | '\u2cc2'            // Coptic
    | '\u2cc4'            // Coptic
    | '\u2cc6'            // Coptic
    | '\u2cc8'            // Coptic
    | '\u2cca'            // Coptic
    | '\u2ccc'            // Coptic
    | '\u2cce'            // Coptic
    | '\u2cd0'            // Coptic
    | '\u2cd2'            // Coptic
    | '\u2cd4'            // Coptic
    | '\u2cd6'            // Coptic
    | '\u2cd8'            // Coptic
    | '\u2cda'            // Coptic
    | '\u2cdc'            // Coptic
    | '\u2cde'            // Coptic
    | '\u2ce0'            // Coptic
    | '\u2ce2'            // Coptic
    | '\u2ceb'            // Coptic
    | '\u2ced'            // Coptic
    | '\u2cf2'            // Coptic
    | '\ua640'            // Cyrillic_Extended-B
    | '\ua642'            // Cyrillic_Extended-B
    | '\ua644'            // Cyrillic_Extended-B
    | '\ua646'            // Cyrillic_Extended-B
    | '\ua648'            // Cyrillic_Extended-B
    | '\ua64a'            // Cyrillic_Extended-B
    | '\ua64c'            // Cyrillic_Extended-B
    | '\ua64e'            // Cyrillic_Extended-B
    | '\ua650'            // Cyrillic_Extended-B
    | '\ua652'            // Cyrillic_Extended-B
    | '\ua654'            // Cyrillic_Extended-B
    | '\ua656'            // Cyrillic_Extended-B
    | '\ua658'            // Cyrillic_Extended-B
    | '\ua65a'            // Cyrillic_Extended-B
    | '\ua65c'            // Cyrillic_Extended-B
    | '\ua65e'            // Cyrillic_Extended-B
    | '\ua660'            // Cyrillic_Extended-B
    | '\ua662'            // Cyrillic_Extended-B
    | '\ua664'            // Cyrillic_Extended-B
    | '\ua666'            // Cyrillic_Extended-B
    | '\ua668'            // Cyrillic_Extended-B
    | '\ua66a'            // Cyrillic_Extended-B
    | '\ua66c'            // Cyrillic_Extended-B
    | '\ua680'            // Cyrillic_Extended-B
    | '\ua682'            // Cyrillic_Extended-B
    | '\ua684'            // Cyrillic_Extended-B
    | '\ua686'            // Cyrillic_Extended-B
    | '\ua688'            // Cyrillic_Extended-B
    | '\ua68a'            // Cyrillic_Extended-B
    | '\ua68c'            // Cyrillic_Extended-B
    | '\ua68e'            // Cyrillic_Extended-B
    | '\ua690'            // Cyrillic_Extended-B
    | '\ua692'            // Cyrillic_Extended-B
    | '\ua694'            // Cyrillic_Extended-B
    | '\ua696'            // Cyrillic_Extended-B
    | '\ua698'            // Cyrillic_Extended-B
    | '\ua69a'            // Cyrillic_Extended-B
    | '\ua722'            // Latin_Extended-D
    | '\ua724'            // Latin_Extended-D
    | '\ua726'            // Latin_Extended-D
    | '\ua728'            // Latin_Extended-D
    | '\ua72a'            // Latin_Extended-D
    | '\ua72c'            // Latin_Extended-D
    | '\ua72e'            // Latin_Extended-D
    | '\ua732'            // Latin_Extended-D
    | '\ua734'            // Latin_Extended-D
    | '\ua736'            // Latin_Extended-D
    | '\ua738'            // Latin_Extended-D
    | '\ua73a'            // Latin_Extended-D
    | '\ua73c'            // Latin_Extended-D
    | '\ua73e'            // Latin_Extended-D
    | '\ua740'            // Latin_Extended-D
    | '\ua742'            // Latin_Extended-D
    | '\ua744'            // Latin_Extended-D
    | '\ua746'            // Latin_Extended-D
    | '\ua748'            // Latin_Extended-D
    | '\ua74a'            // Latin_Extended-D
    | '\ua74c'            // Latin_Extended-D
    | '\ua74e'            // Latin_Extended-D
    | '\ua750'            // Latin_Extended-D
    | '\ua752'            // Latin_Extended-D
    | '\ua754'            // Latin_Extended-D
    | '\ua756'            // Latin_Extended-D
    | '\ua758'            // Latin_Extended-D
    | '\ua75a'            // Latin_Extended-D
    | '\ua75c'            // Latin_Extended-D
    | '\ua75e'            // Latin_Extended-D
    | '\ua760'            // Latin_Extended-D
    | '\ua762'            // Latin_Extended-D
    | '\ua764'            // Latin_Extended-D
    | '\ua766'            // Latin_Extended-D
    | '\ua768'            // Latin_Extended-D
    | '\ua76a'            // Latin_Extended-D
    | '\ua76c'            // Latin_Extended-D
    | '\ua76e'            // Latin_Extended-D
    | '\ua779'            // Latin_Extended-D
    | '\ua77b'            // Latin_Extended-D
    | '\ua77d' ..'\ua77e' // Latin_Extended-D
    | '\ua780'            // Latin_Extended-D
    | '\ua782'            // Latin_Extended-D
    | '\ua784'            // Latin_Extended-D
    | '\ua786'            // Latin_Extended-D
    | '\ua78b'            // Latin_Extended-D
    | '\ua78d'            // Latin_Extended-D
    | '\ua790'            // Latin_Extended-D
    | '\ua792'            // Latin_Extended-D
    | '\ua796'            // Latin_Extended-D
    | '\ua798'            // Latin_Extended-D
    | '\ua79a'            // Latin_Extended-D
    | '\ua79c'            // Latin_Extended-D
    | '\ua79e'            // Latin_Extended-D
    | '\ua7a0'            // Latin_Extended-D
    | '\ua7a2'            // Latin_Extended-D
    | '\ua7a4'            // Latin_Extended-D
    | '\ua7a6'            // Latin_Extended-D
    | '\ua7a8'            // Latin_Extended-D
    | '\ua7aa' ..'\ua7ae' // Latin_Extended-D
    | '\ua7b0' ..'\ua7b4' // Latin_Extended-D
    | '\ua7b6'            // Latin_Extended-D
    | '\uff21' ..'\uff3a' // Halfwidth_and_Fullwidth_Forms
;

fragment SMALL    : ASCSMALL | UNISMALL;
fragment ASCSMALL : [_a-z];
fragment UNISMALL:        // : '\u0061'..'\u007a'       // Basic_Latin
    '\u00b5'              // Latin-1_Supplement
    | '\u00df' ..'\u00f6' // Latin-1_Supplement
    | '\u00f8' ..'\u00ff' // Latin-1_Supplement
    | '\u0101'            // Latin_Extended-A
    | '\u0103'            // Latin_Extended-A
    | '\u0105'            // Latin_Extended-A
    | '\u0107'            // Latin_Extended-A
    | '\u0109'            // Latin_Extended-A
    | '\u010b'            // Latin_Extended-A
    | '\u010d'            // Latin_Extended-A
    | '\u010f'            // Latin_Extended-A
    | '\u0111'            // Latin_Extended-A
    | '\u0113'            // Latin_Extended-A
    | '\u0115'            // Latin_Extended-A
    | '\u0117'            // Latin_Extended-A
    | '\u0119'            // Latin_Extended-A
    | '\u011b'            // Latin_Extended-A
    | '\u011d'            // Latin_Extended-A
    | '\u011f'            // Latin_Extended-A
    | '\u0121'            // Latin_Extended-A
    | '\u0123'            // Latin_Extended-A
    | '\u0125'            // Latin_Extended-A
    | '\u0127'            // Latin_Extended-A
    | '\u0129'            // Latin_Extended-A
    | '\u012b'            // Latin_Extended-A
    | '\u012d'            // Latin_Extended-A
    | '\u012f'            // Latin_Extended-A
    | '\u0131'            // Latin_Extended-A
    | '\u0133'            // Latin_Extended-A
    | '\u0135'            // Latin_Extended-A
    | '\u0137' ..'\u0138' // Latin_Extended-A
    | '\u013a'            // Latin_Extended-A
    | '\u013c'            // Latin_Extended-A
    | '\u013e'            // Latin_Extended-A
    | '\u0140'            // Latin_Extended-A
    | '\u0142'            // Latin_Extended-A
    | '\u0144'            // Latin_Extended-A
    | '\u0146'            // Latin_Extended-A
    | '\u0148' ..'\u0149' // Latin_Extended-A
    | '\u014b'            // Latin_Extended-A
    | '\u014d'            // Latin_Extended-A
    | '\u014f'            // Latin_Extended-A
    | '\u0151'            // Latin_Extended-A
    | '\u0153'            // Latin_Extended-A
    | '\u0155'            // Latin_Extended-A
    | '\u0157'            // Latin_Extended-A
    | '\u0159'            // Latin_Extended-A
    | '\u015b'            // Latin_Extended-A
    | '\u015d'            // Latin_Extended-A
    | '\u015f'            // Latin_Extended-A
    | '\u0161'            // Latin_Extended-A
    | '\u0163'            // Latin_Extended-A
    | '\u0165'            // Latin_Extended-A
    | '\u0167'            // Latin_Extended-A
    | '\u0169'            // Latin_Extended-A
    | '\u016b'            // Latin_Extended-A
    | '\u016d'            // Latin_Extended-A
    | '\u016f'            // Latin_Extended-A
    | '\u0171'            // Latin_Extended-A
    | '\u0173'            // Latin_Extended-A
    | '\u0175'            // Latin_Extended-A
    | '\u0177'            // Latin_Extended-A
    | '\u017a'            // Latin_Extended-A
    | '\u017c'            // Latin_Extended-A
    | '\u017e' ..'\u0180' // Latin_Extended-A
    | '\u0183'            // Latin_Extended-B
    | '\u0185'            // Latin_Extended-B
    | '\u0188'            // Latin_Extended-B
    | '\u018c' ..'\u018d' // Latin_Extended-B
    | '\u0192'            // Latin_Extended-B
    | '\u0195'            // Latin_Extended-B
    | '\u0199' ..'\u019b' // Latin_Extended-B
    | '\u019e'            // Latin_Extended-B
    | '\u01a1'            // Latin_Extended-B
    | '\u01a3'            // Latin_Extended-B
    | '\u01a5'            // Latin_Extended-B
    | '\u01a8'            // Latin_Extended-B
    | '\u01aa' ..'\u01ab' // Latin_Extended-B
    | '\u01ad'            // Latin_Extended-B
    | '\u01b0'            // Latin_Extended-B
    | '\u01b4'            // Latin_Extended-B
    | '\u01b6'            // Latin_Extended-B
    | '\u01b9' ..'\u01ba' // Latin_Extended-B
    | '\u01bd' ..'\u01bf' // Latin_Extended-B
    | '\u01c6'            // Latin_Extended-B
    | '\u01c9'            // Latin_Extended-B
    | '\u01cc'            // Latin_Extended-B
    | '\u01ce'            // Latin_Extended-B
    | '\u01d0'            // Latin_Extended-B
    | '\u01d2'            // Latin_Extended-B
    | '\u01d4'            // Latin_Extended-B
    | '\u01d6'            // Latin_Extended-B
    | '\u01d8'            // Latin_Extended-B
    | '\u01da'            // Latin_Extended-B
    | '\u01dc' ..'\u01dd' // Latin_Extended-B
    | '\u01df'            // Latin_Extended-B
    | '\u01e1'            // Latin_Extended-B
    | '\u01e3'            // Latin_Extended-B
    | '\u01e5'            // Latin_Extended-B
    | '\u01e7'            // Latin_Extended-B
    | '\u01e9'            // Latin_Extended-B
    | '\u01eb'            // Latin_Extended-B
    | '\u01ed'            // Latin_Extended-B
    | '\u01ef' ..'\u01f0' // Latin_Extended-B
    | '\u01f3'            // Latin_Extended-B
    | '\u01f5'            // Latin_Extended-B
    | '\u01f9'            // Latin_Extended-B
    | '\u01fb'            // Latin_Extended-B
    | '\u01fd'            // Latin_Extended-B
    | '\u01ff'            // Latin_Extended-B
    | '\u0201'            // Latin_Extended-B
    | '\u0203'            // Latin_Extended-B
    | '\u0205'            // Latin_Extended-B
    | '\u0207'            // Latin_Extended-B
    | '\u0209'            // Latin_Extended-B
    | '\u020b'            // Latin_Extended-B
    | '\u020d'            // Latin_Extended-B
    | '\u020f'            // Latin_Extended-B
    | '\u0211'            // Latin_Extended-B
    | '\u0213'            // Latin_Extended-B
    | '\u0215'            // Latin_Extended-B
    | '\u0217'            // Latin_Extended-B
    | '\u0219'            // Latin_Extended-B
    | '\u021b'            // Latin_Extended-B
    | '\u021d'            // Latin_Extended-B
    | '\u021f'            // Latin_Extended-B
    | '\u0221'            // Latin_Extended-B
    | '\u0223'            // Latin_Extended-B
    | '\u0225'            // Latin_Extended-B
    | '\u0227'            // Latin_Extended-B
    | '\u0229'            // Latin_Extended-B
    | '\u022b'            // Latin_Extended-B
    | '\u022d'            // Latin_Extended-B
    | '\u022f'            // Latin_Extended-B
    | '\u0231'            // Latin_Extended-B
    | '\u0233' ..'\u0239' // Latin_Extended-B
    | '\u023c'            // Latin_Extended-B
    | '\u023f' ..'\u0240' // Latin_Extended-B
    | '\u0242'            // Latin_Extended-B
    | '\u0247'            // Latin_Extended-B
    | '\u0249'            // Latin_Extended-B
    | '\u024b'            // Latin_Extended-B
    | '\u024d'            // Latin_Extended-B
    | '\u024f' ..'\u0293' // (Absent from Blocks.txt)
    | '\u0295' ..'\u02af' // IPA_Extensions
    | '\u0371'            // Greek_and_Coptic
    | '\u0373'            // Greek_and_Coptic
    | '\u0377'            // Greek_and_Coptic
    | '\u037b' ..'\u037d' // Greek_and_Coptic
    | '\u0390'            // Greek_and_Coptic
    | '\u03ac' ..'\u03ce' // Greek_and_Coptic
    | '\u03d0' ..'\u03d1' // Greek_and_Coptic
    | '\u03d5' ..'\u03d7' // Greek_and_Coptic
    | '\u03d9'            // Greek_and_Coptic
    | '\u03db'            // Greek_and_Coptic
    | '\u03dd'            // Greek_and_Coptic
    | '\u03df'            // Greek_and_Coptic
    | '\u03e1'            // Greek_and_Coptic
    | '\u03e3'            // Greek_and_Coptic
    | '\u03e5'            // Greek_and_Coptic
    | '\u03e7'            // Greek_and_Coptic
    | '\u03e9'            // Greek_and_Coptic
    | '\u03eb'            // Greek_and_Coptic
    | '\u03ed'            // Greek_and_Coptic
    | '\u03ef' ..'\u03f3' // Greek_and_Coptic
    | '\u03f5'            // Greek_and_Coptic
    | '\u03f8'            // Greek_and_Coptic
    | '\u03fb' ..'\u03fc' // Greek_and_Coptic
    | '\u0430' ..'\u045f' // Cyrillic
    | '\u0461'            // Cyrillic
    | '\u0463'            // Cyrillic
    | '\u0465'            // Cyrillic
    | '\u0467'            // Cyrillic
    | '\u0469'            // Cyrillic
    | '\u046b'            // Cyrillic
    | '\u046d'            // Cyrillic
    | '\u046f'            // Cyrillic
    | '\u0471'            // Cyrillic
    | '\u0473'            // Cyrillic
    | '\u0475'            // Cyrillic
    | '\u0477'            // Cyrillic
    | '\u0479'            // Cyrillic
    | '\u047b'            // Cyrillic
    | '\u047d'            // Cyrillic
    | '\u047f'            // Cyrillic
    | '\u0481'            // Cyrillic
    | '\u048b'            // Cyrillic
    | '\u048d'            // Cyrillic
    | '\u048f'            // Cyrillic
    | '\u0491'            // Cyrillic
    | '\u0493'            // Cyrillic
    | '\u0495'            // Cyrillic
    | '\u0497'            // Cyrillic
    | '\u0499'            // Cyrillic
    | '\u049b'            // Cyrillic
    | '\u049d'            // Cyrillic
    | '\u049f'            // Cyrillic
    | '\u04a1'            // Cyrillic
    | '\u04a3'            // Cyrillic
    | '\u04a5'            // Cyrillic
    | '\u04a7'            // Cyrillic
    | '\u04a9'            // Cyrillic
    | '\u04ab'            // Cyrillic
    | '\u04ad'            // Cyrillic
    | '\u04af'            // Cyrillic
    | '\u04b1'            // Cyrillic
    | '\u04b3'            // Cyrillic
    | '\u04b5'            // Cyrillic
    | '\u04b7'            // Cyrillic
    | '\u04b9'            // Cyrillic
    | '\u04bb'            // Cyrillic
    | '\u04bd'            // Cyrillic
    | '\u04bf'            // Cyrillic
    | '\u04c2'            // Cyrillic
    | '\u04c4'            // Cyrillic
    | '\u04c6'            // Cyrillic
    | '\u04c8'            // Cyrillic
    | '\u04ca'            // Cyrillic
    | '\u04cc'            // Cyrillic
    | '\u04ce' ..'\u04cf' // Cyrillic
    | '\u04d1'            // Cyrillic
    | '\u04d3'            // Cyrillic
    | '\u04d5'            // Cyrillic
    | '\u04d7'            // Cyrillic
    | '\u04d9'            // Cyrillic
    | '\u04db'            // Cyrillic
    | '\u04dd'            // Cyrillic
    | '\u04df'            // Cyrillic
    | '\u04e1'            // Cyrillic
    | '\u04e3'            // Cyrillic
    | '\u04e5'            // Cyrillic
    | '\u04e7'            // Cyrillic
    | '\u04e9'            // Cyrillic
    | '\u04eb'            // Cyrillic
    | '\u04ed'            // Cyrillic
    | '\u04ef'            // Cyrillic
    | '\u04f1'            // Cyrillic
    | '\u04f3'            // Cyrillic
    | '\u04f5'            // Cyrillic
    | '\u04f7'            // Cyrillic
    | '\u04f9'            // Cyrillic
    | '\u04fb'            // Cyrillic
    | '\u04fd'            // Cyrillic
    | '\u04ff'            // (Absent from Blocks.txt)
    | '\u0501'            // Cyrillic_Supplement
    | '\u0503'            // Cyrillic_Supplement
    | '\u0505'            // Cyrillic_Supplement
    | '\u0507'            // Cyrillic_Supplement
    | '\u0509'            // Cyrillic_Supplement
    | '\u050b'            // Cyrillic_Supplement
    | '\u050d'            // Cyrillic_Supplement
    | '\u050f'            // Cyrillic_Supplement
    | '\u0511'            // Cyrillic_Supplement
    | '\u0513'            // Cyrillic_Supplement
    | '\u0515'            // Cyrillic_Supplement
    | '\u0517'            // Cyrillic_Supplement
    | '\u0519'            // Cyrillic_Supplement
    | '\u051b'            // Cyrillic_Supplement
    | '\u051d'            // Cyrillic_Supplement
    | '\u051f'            // Cyrillic_Supplement
    | '\u0521'            // Cyrillic_Supplement
    | '\u0523'            // Cyrillic_Supplement
    | '\u0525'            // Cyrillic_Supplement
    | '\u0527'            // Cyrillic_Supplement
    | '\u0529'            // Cyrillic_Supplement
    | '\u052b'            // Cyrillic_Supplement
    | '\u052d'            // Cyrillic_Supplement
    | '\u052f'            // (Absent from Blocks.txt)
    | '\u0561' ..'\u0587' // Armenian
    | '\u13f8' ..'\u13fd' // Cherokee
    | '\u1c80' ..'\u1c88' // Cyrillic_Extended-C
    | '\u1d00' ..'\u1d2b' // Phonetic_Extensions
    | '\u1d6b' ..'\u1d77' // Phonetic_Extensions
    | '\u1d79' ..'\u1d9a' // Phonetic_Extensions
    | '\u1e01'            // Latin_Extended_Additional
    | '\u1e03'            // Latin_Extended_Additional
    | '\u1e05'            // Latin_Extended_Additional
    | '\u1e07'            // Latin_Extended_Additional
    | '\u1e09'            // Latin_Extended_Additional
    | '\u1e0b'            // Latin_Extended_Additional
    | '\u1e0d'            // Latin_Extended_Additional
    | '\u1e0f'            // Latin_Extended_Additional
    | '\u1e11'            // Latin_Extended_Additional
    | '\u1e13'            // Latin_Extended_Additional
    | '\u1e15'            // Latin_Extended_Additional
    | '\u1e17'            // Latin_Extended_Additional
    | '\u1e19'            // Latin_Extended_Additional
    | '\u1e1b'            // Latin_Extended_Additional
    | '\u1e1d'            // Latin_Extended_Additional
    | '\u1e1f'            // Latin_Extended_Additional
    | '\u1e21'            // Latin_Extended_Additional
    | '\u1e23'            // Latin_Extended_Additional
    | '\u1e25'            // Latin_Extended_Additional
    | '\u1e27'            // Latin_Extended_Additional
    | '\u1e29'            // Latin_Extended_Additional
    | '\u1e2b'            // Latin_Extended_Additional
    | '\u1e2d'            // Latin_Extended_Additional
    | '\u1e2f'            // Latin_Extended_Additional
    | '\u1e31'            // Latin_Extended_Additional
    | '\u1e33'            // Latin_Extended_Additional
    | '\u1e35'            // Latin_Extended_Additional
    | '\u1e37'            // Latin_Extended_Additional
    | '\u1e39'            // Latin_Extended_Additional
    | '\u1e3b'            // Latin_Extended_Additional
    | '\u1e3d'            // Latin_Extended_Additional
    | '\u1e3f'            // Latin_Extended_Additional
    | '\u1e41'            // Latin_Extended_Additional
    | '\u1e43'            // Latin_Extended_Additional
    | '\u1e45'            // Latin_Extended_Additional
    | '\u1e47'            // Latin_Extended_Additional
    | '\u1e49'            // Latin_Extended_Additional
    | '\u1e4b'            // Latin_Extended_Additional
    | '\u1e4d'            // Latin_Extended_Additional
    | '\u1e4f'            // Latin_Extended_Additional
    | '\u1e51'            // Latin_Extended_Additional
    | '\u1e53'            // Latin_Extended_Additional
    | '\u1e55'            // Latin_Extended_Additional
    | '\u1e57'            // Latin_Extended_Additional
    | '\u1e59'            // Latin_Extended_Additional
    | '\u1e5b'            // Latin_Extended_Additional
    | '\u1e5d'            // Latin_Extended_Additional
    | '\u1e5f'            // Latin_Extended_Additional
    | '\u1e61'            // Latin_Extended_Additional
    | '\u1e63'            // Latin_Extended_Additional
    | '\u1e65'            // Latin_Extended_Additional
    | '\u1e67'            // Latin_Extended_Additional
    | '\u1e69'            // Latin_Extended_Additional
    | '\u1e6b'            // Latin_Extended_Additional
    | '\u1e6d'            // Latin_Extended_Additional
    | '\u1e6f'            // Latin_Extended_Additional
    | '\u1e71'            // Latin_Extended_Additional
    | '\u1e73'            // Latin_Extended_Additional
    | '\u1e75'            // Latin_Extended_Additional
    | '\u1e77'            // Latin_Extended_Additional
    | '\u1e79'            // Latin_Extended_Additional
    | '\u1e7b'            // Latin_Extended_Additional
    | '\u1e7d'            // Latin_Extended_Additional
    | '\u1e7f'            // Latin_Extended_Additional
    | '\u1e81'            // Latin_Extended_Additional
    | '\u1e83'            // Latin_Extended_Additional
    | '\u1e85'            // Latin_Extended_Additional
    | '\u1e87'            // Latin_Extended_Additional
    | '\u1e89'            // Latin_Extended_Additional
    | '\u1e8b'            // Latin_Extended_Additional
    | '\u1e8d'            // Latin_Extended_Additional
    | '\u1e8f'            // Latin_Extended_Additional
    | '\u1e91'            // Latin_Extended_Additional
    | '\u1e93'            // Latin_Extended_Additional
    | '\u1e95' ..'\u1e9d' // Latin_Extended_Additional
    | '\u1e9f'            // Latin_Extended_Additional
    | '\u1ea1'            // Latin_Extended_Additional
    | '\u1ea3'            // Latin_Extended_Additional
    | '\u1ea5'            // Latin_Extended_Additional
    | '\u1ea7'            // Latin_Extended_Additional
    | '\u1ea9'            // Latin_Extended_Additional
    | '\u1eab'            // Latin_Extended_Additional
    | '\u1ead'            // Latin_Extended_Additional
    | '\u1eaf'            // Latin_Extended_Additional
    | '\u1eb1'            // Latin_Extended_Additional
    | '\u1eb3'            // Latin_Extended_Additional
    | '\u1eb5'            // Latin_Extended_Additional
    | '\u1eb7'            // Latin_Extended_Additional
    | '\u1eb9'            // Latin_Extended_Additional
    | '\u1ebb'            // Latin_Extended_Additional
    | '\u1ebd'            // Latin_Extended_Additional
    | '\u1ebf'            // Latin_Extended_Additional
    | '\u1ec1'            // Latin_Extended_Additional
    | '\u1ec3'            // Latin_Extended_Additional
    | '\u1ec5'            // Latin_Extended_Additional
    | '\u1ec7'            // Latin_Extended_Additional
    | '\u1ec9'            // Latin_Extended_Additional
    | '\u1ecb'            // Latin_Extended_Additional
    | '\u1ecd'            // Latin_Extended_Additional
    | '\u1ecf'            // Latin_Extended_Additional
    | '\u1ed1'            // Latin_Extended_Additional
    | '\u1ed3'            // Latin_Extended_Additional
    | '\u1ed5'            // Latin_Extended_Additional
    | '\u1ed7'            // Latin_Extended_Additional
    | '\u1ed9'            // Latin_Extended_Additional
    | '\u1edb'            // Latin_Extended_Additional
    | '\u1edd'            // Latin_Extended_Additional
    | '\u1edf'            // Latin_Extended_Additional
    | '\u1ee1'            // Latin_Extended_Additional
    | '\u1ee3'            // Latin_Extended_Additional
    | '\u1ee5'            // Latin_Extended_Additional
    | '\u1ee7'            // Latin_Extended_Additional
    | '\u1ee9'            // Latin_Extended_Additional
    | '\u1eeb'            // Latin_Extended_Additional
    | '\u1eed'            // Latin_Extended_Additional
    | '\u1eef'            // Latin_Extended_Additional
    | '\u1ef1'            // Latin_Extended_Additional
    | '\u1ef3'            // Latin_Extended_Additional
    | '\u1ef5'            // Latin_Extended_Additional
    | '\u1ef7'            // Latin_Extended_Additional
    | '\u1ef9'            // Latin_Extended_Additional
    | '\u1efb'            // Latin_Extended_Additional
    | '\u1efd'            // Latin_Extended_Additional
    | '\u1eff' ..'\u1f07' // (Absent from Blocks.txt)
    | '\u1f10' ..'\u1f15' // Greek_Extended
    | '\u1f20' ..'\u1f27' // Greek_Extended
    | '\u1f30' ..'\u1f37' // Greek_Extended
    | '\u1f40' ..'\u1f45' // Greek_Extended
    | '\u1f50' ..'\u1f57' // Greek_Extended
    | '\u1f60' ..'\u1f67' // Greek_Extended
    | '\u1f70' ..'\u1f7d' // Greek_Extended
    | '\u1f80' ..'\u1f87' // Greek_Extended
    | '\u1f90' ..'\u1f97' // Greek_Extended
    | '\u1fa0' ..'\u1fa7' // Greek_Extended
    | '\u1fb0' ..'\u1fb4' // Greek_Extended
    | '\u1fb6' ..'\u1fb7' // Greek_Extended
    | '\u1fbe'            // Greek_Extended
    | '\u1fc2' ..'\u1fc4' // Greek_Extended
    | '\u1fc6' ..'\u1fc7' // Greek_Extended
    | '\u1fd0' ..'\u1fd3' // Greek_Extended
    | '\u1fd6' ..'\u1fd7' // Greek_Extended
    | '\u1fe0' ..'\u1fe7' // Greek_Extended
    | '\u1ff2' ..'\u1ff4' // Greek_Extended
    | '\u1ff6' ..'\u1ff7' // Greek_Extended
    | '\u210a'            // Letterlike_Symbols
    | '\u210e' ..'\u210f' // Letterlike_Symbols
    | '\u2113'            // Letterlike_Symbols
    | '\u212f'            // Letterlike_Symbols
    | '\u2134'            // Letterlike_Symbols
    | '\u2139'            // Letterlike_Symbols
    | '\u213c' ..'\u213d' // Letterlike_Symbols
    | '\u2146' ..'\u2149' // Letterlike_Symbols
    | '\u214e'            // Letterlike_Symbols
    | '\u2184'            // Number_Forms
    | '\u2c30' ..'\u2c5e' // Glagolitic
    | '\u2c61'            // Latin_Extended-C
    | '\u2c65' ..'\u2c66' // Latin_Extended-C
    | '\u2c68'            // Latin_Extended-C
    | '\u2c6a'            // Latin_Extended-C
    | '\u2c6c'            // Latin_Extended-C
    | '\u2c71'            // Latin_Extended-C
    | '\u2c73' ..'\u2c74' // Latin_Extended-C
    | '\u2c76' ..'\u2c7b' // Latin_Extended-C
    | '\u2c81'            // Coptic
    | '\u2c83'            // Coptic
    | '\u2c85'            // Coptic
    | '\u2c87'            // Coptic
    | '\u2c89'            // Coptic
    | '\u2c8b'            // Coptic
    | '\u2c8d'            // Coptic
    | '\u2c8f'            // Coptic
    | '\u2c91'            // Coptic
    | '\u2c93'            // Coptic
    | '\u2c95'            // Coptic
    | '\u2c97'            // Coptic
    | '\u2c99'            // Coptic
    | '\u2c9b'            // Coptic
    | '\u2c9d'            // Coptic
    | '\u2c9f'            // Coptic
    | '\u2ca1'            // Coptic
    | '\u2ca3'            // Coptic
    | '\u2ca5'            // Coptic
    | '\u2ca7'            // Coptic
    | '\u2ca9'            // Coptic
    | '\u2cab'            // Coptic
    | '\u2cad'            // Coptic
    | '\u2caf'            // Coptic
    | '\u2cb1'            // Coptic
    | '\u2cb3'            // Coptic
    | '\u2cb5'            // Coptic
    | '\u2cb7'            // Coptic
    | '\u2cb9'            // Coptic
    | '\u2cbb'            // Coptic
    | '\u2cbd'            // Coptic
    | '\u2cbf'            // Coptic
    | '\u2cc1'            // Coptic
    | '\u2cc3'            // Coptic
    | '\u2cc5'            // Coptic
    | '\u2cc7'            // Coptic
    | '\u2cc9'            // Coptic
    | '\u2ccb'            // Coptic
    | '\u2ccd'            // Coptic
    | '\u2ccf'            // Coptic
    | '\u2cd1'            // Coptic
    | '\u2cd3'            // Coptic
    | '\u2cd5'            // Coptic
    | '\u2cd7'            // Coptic
    | '\u2cd9'            // Coptic
    | '\u2cdb'            // Coptic
    | '\u2cdd'            // Coptic
    | '\u2cdf'            // Coptic
    | '\u2ce1'            // Coptic
    | '\u2ce3' ..'\u2ce4' // Coptic
    | '\u2cec'            // Coptic
    | '\u2cee'            // Coptic
    | '\u2cf3'            // Coptic
    | '\u2d00' ..'\u2d25' // Georgian_Supplement
    | '\u2d27'            // Georgian_Supplement
    | '\u2d2d'            // Georgian_Supplement
    | '\ua641'            // Cyrillic_Extended-B
    | '\ua643'            // Cyrillic_Extended-B
    | '\ua645'            // Cyrillic_Extended-B
    | '\ua647'            // Cyrillic_Extended-B
    | '\ua649'            // Cyrillic_Extended-B
    | '\ua64b'            // Cyrillic_Extended-B
    | '\ua64d'            // Cyrillic_Extended-B
    | '\ua64f'            // Cyrillic_Extended-B
    | '\ua651'            // Cyrillic_Extended-B
    | '\ua653'            // Cyrillic_Extended-B
    | '\ua655'            // Cyrillic_Extended-B
    | '\ua657'            // Cyrillic_Extended-B
    | '\ua659'            // Cyrillic_Extended-B
    | '\ua65b'            // Cyrillic_Extended-B
    | '\ua65d'            // Cyrillic_Extended-B
    | '\ua65f'            // Cyrillic_Extended-B
    | '\ua661'            // Cyrillic_Extended-B
    | '\ua663'            // Cyrillic_Extended-B
    | '\ua665'            // Cyrillic_Extended-B
    | '\ua667'            // Cyrillic_Extended-B
    | '\ua669'            // Cyrillic_Extended-B
    | '\ua66b'            // Cyrillic_Extended-B
    | '\ua66d'            // Cyrillic_Extended-B
    | '\ua681'            // Cyrillic_Extended-B
    | '\ua683'            // Cyrillic_Extended-B
    | '\ua685'            // Cyrillic_Extended-B
    | '\ua687'            // Cyrillic_Extended-B
    | '\ua689'            // Cyrillic_Extended-B
    | '\ua68b'            // Cyrillic_Extended-B
    | '\ua68d'            // Cyrillic_Extended-B
    | '\ua68f'            // Cyrillic_Extended-B
    | '\ua691'            // Cyrillic_Extended-B
    | '\ua693'            // Cyrillic_Extended-B
    | '\ua695'            // Cyrillic_Extended-B
    | '\ua697'            // Cyrillic_Extended-B
    | '\ua699'            // Cyrillic_Extended-B
    | '\ua69b'            // Cyrillic_Extended-B
    | '\ua723'            // Latin_Extended-D
    | '\ua725'            // Latin_Extended-D
    | '\ua727'            // Latin_Extended-D
    | '\ua729'            // Latin_Extended-D
    | '\ua72b'            // Latin_Extended-D
    | '\ua72d'            // Latin_Extended-D
    | '\ua72f' ..'\ua731' // Latin_Extended-D
    | '\ua733'            // Latin_Extended-D
    | '\ua735'            // Latin_Extended-D
    | '\ua737'            // Latin_Extended-D
    | '\ua739'            // Latin_Extended-D
    | '\ua73b'            // Latin_Extended-D
    | '\ua73d'            // Latin_Extended-D
    | '\ua73f'            // Latin_Extended-D
    | '\ua741'            // Latin_Extended-D
    | '\ua743'            // Latin_Extended-D
    | '\ua745'            // Latin_Extended-D
    | '\ua747'            // Latin_Extended-D
    | '\ua749'            // Latin_Extended-D
    | '\ua74b'            // Latin_Extended-D
    | '\ua74d'            // Latin_Extended-D
    | '\ua74f'            // Latin_Extended-D
    | '\ua751'            // Latin_Extended-D
    | '\ua753'            // Latin_Extended-D
    | '\ua755'            // Latin_Extended-D
    | '\ua757'            // Latin_Extended-D
    | '\ua759'            // Latin_Extended-D
    | '\ua75b'            // Latin_Extended-D
    | '\ua75d'            // Latin_Extended-D
    | '\ua75f'            // Latin_Extended-D
    | '\ua761'            // Latin_Extended-D
    | '\ua763'            // Latin_Extended-D
    | '\ua765'            // Latin_Extended-D
    | '\ua767'            // Latin_Extended-D
    | '\ua769'            // Latin_Extended-D
    | '\ua76b'            // Latin_Extended-D
    | '\ua76d'            // Latin_Extended-D
    | '\ua76f'            // Latin_Extended-D
    | '\ua771' ..'\ua778' // Latin_Extended-D
    | '\ua77a'            // Latin_Extended-D
    | '\ua77c'            // Latin_Extended-D
    | '\ua77f'            // Latin_Extended-D
    | '\ua781'            // Latin_Extended-D
    | '\ua783'            // Latin_Extended-D
    | '\ua785'            // Latin_Extended-D
    | '\ua787'            // Latin_Extended-D
    | '\ua78c'            // Latin_Extended-D
    | '\ua78e'            // Latin_Extended-D
    | '\ua791'            // Latin_Extended-D
    | '\ua793' ..'\ua795' // Latin_Extended-D
    | '\ua797'            // Latin_Extended-D
    | '\ua799'            // Latin_Extended-D
    | '\ua79b'            // Latin_Extended-D
    | '\ua79d'            // Latin_Extended-D
    | '\ua79f'            // Latin_Extended-D
    | '\ua7a1'            // Latin_Extended-D
    | '\ua7a3'            // Latin_Extended-D
    | '\ua7a5'            // Latin_Extended-D
    | '\ua7a7'            // Latin_Extended-D
    | '\ua7a9'            // Latin_Extended-D
    | '\ua7b5'            // Latin_Extended-D
    | '\ua7b7'            // Latin_Extended-D
    | '\ua7fa'            // Latin_Extended-D
    | '\uab30' ..'\uab5a' // Latin_Extended-E
    | '\uab60' ..'\uab65' // Latin_Extended-E
    | '\uab70' ..'\uabbf' // Cherokee_Supplement
    | '\ufb00' ..'\ufb06' // Alphabetic_Presentation_Forms
    | '\ufb13' ..'\ufb17' // Alphabetic_Presentation_Forms
    | '\uff41' ..'\uff5a' // Halfwidth_and_Fullwidth_Forms
;

fragment ASCSYMBOL:
    '!'
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
    | '-'
    | '~'
    | ':'
;

fragment UNISYMBOL: CLASSIFY_Sc | CLASSIFY_Sk | CLASSIFY_Sm | CLASSIFY_So;

fragment CLASSIFY_Sc:
    '\u0024'              // Basic_Latin
    | '\u00a2' ..'\u00a5' // Latin-1_Supplement
    | '\u058f'            // (Absent from Blocks.txt)
    | '\u060b'            // Arabic
    | '\u09f2' ..'\u09f3' // Bengali
    | '\u09fb'            // Bengali
    | '\u0af1'            // Gujarati
    | '\u0bf9'            // Tamil
    | '\u0e3f'            // Thai
    | '\u17db'            // Khmer
    | '\u20a0' ..'\u20be' // Currency_Symbols
    | '\ua838'            // Common_Indic_Number_Forms
    | '\ufdfc'            // Arabic_Presentation_Forms-A
    | '\ufe69'            // Small_Form_Variants
    | '\uff04'            // Halfwidth_and_Fullwidth_Forms
    | '\uffe0' ..'\uffe1' // Halfwidth_and_Fullwidth_Forms
    | '\uffe5' ..'\uffe6' // Halfwidth_and_Fullwidth_Forms
;

fragment CLASSIFY_Sk:
    '\u005e'              // Basic_Latin
    | '\u0060'            // Basic_Latin
    | '\u00a8'            // Latin-1_Supplement
    | '\u00af'            // Latin-1_Supplement
    | '\u00b4'            // Latin-1_Supplement
    | '\u00b8'            // Latin-1_Supplement
    | '\u02c2' ..'\u02c5' // Spacing_Modifier_Letters
    | '\u02d2' ..'\u02df' // Spacing_Modifier_Letters
    | '\u02e5' ..'\u02eb' // Spacing_Modifier_Letters
    | '\u02ed'            // Spacing_Modifier_Letters
    | '\u02ef' ..'\u02ff' // Spacing_Modifier_Letters
    | '\u0375'            // Greek_and_Coptic
    | '\u0384' ..'\u0385' // Greek_and_Coptic
    | '\u1fbd'            // Greek_Extended
    | '\u1fbf' ..'\u1fc1' // Greek_Extended
    | '\u1fcd' ..'\u1fcf' // Greek_Extended
    | '\u1fdd' ..'\u1fdf' // Greek_Extended
    | '\u1fed' ..'\u1fef' // Greek_Extended
    | '\u1ffd' ..'\u1ffe' // Greek_Extended
    | '\u309b' ..'\u309c' // Hiragana
    | '\ua700' ..'\ua716' // Modifier_Tone_Letters
    | '\ua720' ..'\ua721' // Latin_Extended-D
    | '\ua789' ..'\ua78a' // Latin_Extended-D
    | '\uab5b'            // Latin_Extended-E
    | '\ufbb2' ..'\ufbc1' // Arabic_Presentation_Forms-A
    | '\uff3e'            // Halfwidth_and_Fullwidth_Forms
    | '\uff40'            // Halfwidth_and_Fullwidth_Forms
    | '\uffe3'            // Halfwidth_and_Fullwidth_Forms
;

fragment CLASSIFY_Sm:
    '\u002b'              // Basic_Latin
    | '\u003c' ..'\u003e' // Basic_Latin
    | '\u007c'            // Basic_Latin
    | '\u007e'            // Basic_Latin
    | '\u00ac'            // Latin-1_Supplement
    | '\u00b1'            // Latin-1_Supplement
    | '\u00d7'            // Latin-1_Supplement
    | '\u00f7'            // Latin-1_Supplement
    | '\u03f6'            // Greek_and_Coptic
    | '\u0606' ..'\u0608' // Arabic
    | '\u2044'            // General_Punctuation
    | '\u2052'            // General_Punctuation
    | '\u207a' ..'\u207c' // Superscripts_and_Subscripts
    | '\u208a' ..'\u208c' // Superscripts_and_Subscripts
    | '\u2118'            // Letterlike_Symbols
    | '\u2140' ..'\u2144' // Letterlike_Symbols
    | '\u214b'            // Letterlike_Symbols
    | '\u2190' ..'\u2194' // Arrows
    | '\u219a' ..'\u219b' // Arrows
    | '\u21a0'            // Arrows
    | '\u21a3'            // Arrows
    | '\u21a6'            // Arrows
    | '\u21ae'            // Arrows
    | '\u21ce' ..'\u21cf' // Arrows
    | '\u21d2'            // Arrows
    | '\u21d4'            // Arrows
    | '\u21f4' ..'\u22ff' // Arrows
    | '\u2320' ..'\u2321' // Miscellaneous_Technical
    | '\u237c'            // Miscellaneous_Technical
    | '\u239b' ..'\u23b3' // Miscellaneous_Technical
    | '\u23dc' ..'\u23e1' // Miscellaneous_Technical
    | '\u25b7'            // Geometric_Shapes
    | '\u25c1'            // Geometric_Shapes
    | '\u25f8' ..'\u25ff' // Geometric_Shapes
    | '\u266f'            // Miscellaneous_Symbols
    | '\u27c0' ..'\u27c4' // Miscellaneous_Mathematical_Symbols-A
    | '\u27c7' ..'\u27e5' // Miscellaneous_Mathematical_Symbols-A
    | '\u27f0' ..'\u27ff' // Supplemental_Arrows-A
    | '\u2900' ..'\u2982' // Supplemental_Arrows-B
    | '\u2999' ..'\u29d7' // Miscellaneous_Mathematical_Symbols-B
    | '\u29dc' ..'\u29fb' // Miscellaneous_Mathematical_Symbols-B
    | '\u29fe' ..'\u2aff' // Miscellaneous_Mathematical_Symbols-B
    | '\u2b30' ..'\u2b44' // Miscellaneous_Symbols_and_Arrows
    | '\u2b47' ..'\u2b4c' // Miscellaneous_Symbols_and_Arrows
    | '\ufb29'            // Alphabetic_Presentation_Forms
    | '\ufe62'            // Small_Form_Variants
    | '\ufe64' ..'\ufe66' // Small_Form_Variants
    | '\uff0b'            // Halfwidth_and_Fullwidth_Forms
    | '\uff1c' ..'\uff1e' // Halfwidth_and_Fullwidth_Forms
    | '\uff5c'            // Halfwidth_and_Fullwidth_Forms
    | '\uff5e'            // Halfwidth_and_Fullwidth_Forms
    | '\uffe2'            // Halfwidth_and_Fullwidth_Forms
    | '\uffe9' ..'\uffec' // Halfwidth_and_Fullwidth_Forms
;

fragment CLASSIFY_So:
    '\u00a6'              // Latin-1_Supplement
    | '\u00a9'            // Latin-1_Supplement
    | '\u00ae'            // Latin-1_Supplement
    | '\u00b0'            // Latin-1_Supplement
    | '\u0482'            // Cyrillic
    | '\u058d' ..'\u058e' // Armenian
    | '\u060e' ..'\u060f' // Arabic
    | '\u06de'            // Arabic
    | '\u06e9'            // Arabic
    | '\u06fd' ..'\u06fe' // Arabic
    | '\u07f6'            // NKo
    | '\u09fa'            // Bengali
    | '\u0b70'            // Oriya
    | '\u0bf3' ..'\u0bf8' // Tamil
    | '\u0bfa'            // Tamil
    | '\u0c7f'            // (Absent from Blocks.txt)
    | '\u0d4f'            // Malayalam
    | '\u0d79'            // Malayalam
    | '\u0f01' ..'\u0f03' // Tibetan
    | '\u0f13'            // Tibetan
    | '\u0f15' ..'\u0f17' // Tibetan
    | '\u0f1a' ..'\u0f1f' // Tibetan
    | '\u0f34'            // Tibetan
    | '\u0f36'            // Tibetan
    | '\u0f38'            // Tibetan
    | '\u0fbe' ..'\u0fc5' // Tibetan
    | '\u0fc7' ..'\u0fcc' // Tibetan
    | '\u0fce' ..'\u0fcf' // Tibetan
    | '\u0fd5' ..'\u0fd8' // Tibetan
    | '\u109e' ..'\u109f' // Myanmar
    | '\u1390' ..'\u1399' // Ethiopic_Supplement
    | '\u1940'            // Limbu
    | '\u19de' ..'\u19ff' // New_Tai_Lue
    | '\u1b61' ..'\u1b6a' // Balinese
    | '\u1b74' ..'\u1b7c' // Balinese
    | '\u2100' ..'\u2101' // Letterlike_Symbols
    | '\u2103' ..'\u2106' // Letterlike_Symbols
    | '\u2108' ..'\u2109' // Letterlike_Symbols
    | '\u2114'            // Letterlike_Symbols
    | '\u2116' ..'\u2117' // Letterlike_Symbols
    | '\u211e' ..'\u2123' // Letterlike_Symbols
    | '\u2125'            // Letterlike_Symbols
    | '\u2127'            // Letterlike_Symbols
    | '\u2129'            // Letterlike_Symbols
    | '\u212e'            // Letterlike_Symbols
    | '\u213a' ..'\u213b' // Letterlike_Symbols
    | '\u214a'            // Letterlike_Symbols
    | '\u214c' ..'\u214d' // Letterlike_Symbols
    | '\u214f'            // (Absent from Blocks.txt)
    | '\u218a' ..'\u218b' // Number_Forms
    | '\u2195' ..'\u2199' // Arrows
    | '\u219c' ..'\u219f' // Arrows
    | '\u21a1' ..'\u21a2' // Arrows
    | '\u21a4' ..'\u21a5' // Arrows
    | '\u21a7' ..'\u21ad' // Arrows
    | '\u21af' ..'\u21cd' // Arrows
    | '\u21d0' ..'\u21d1' // Arrows
    | '\u21d3'            // Arrows
    | '\u21d5' ..'\u21f3' // Arrows
    | '\u2300' ..'\u2307' // Miscellaneous_Technical
    | '\u230c' ..'\u231f' // Miscellaneous_Technical
    | '\u2322' ..'\u2328' // Miscellaneous_Technical
    | '\u232b' ..'\u237b' // Miscellaneous_Technical
    | '\u237d' ..'\u239a' // Miscellaneous_Technical
    | '\u23b4' ..'\u23db' // Miscellaneous_Technical
    | '\u23e2' ..'\u23fe' // Miscellaneous_Technical
    | '\u2400' ..'\u2426' // Control_Pictures
    | '\u2440' ..'\u244a' // Optical_Character_Recognition
    | '\u249c' ..'\u24e9' // Enclosed_Alphanumerics
    | '\u2500' ..'\u25b6' // Box_Drawing
    | '\u25b8' ..'\u25c0' // Geometric_Shapes
    | '\u25c2' ..'\u25f7' // Geometric_Shapes
    | '\u2600' ..'\u266e' // Miscellaneous_Symbols
    | '\u2670' ..'\u2767' // Miscellaneous_Symbols
    | '\u2794' ..'\u27bf' // Dingbats
    | '\u2800' ..'\u28ff' // Braille_Patterns
    | '\u2b00' ..'\u2b2f' // Miscellaneous_Symbols_and_Arrows
    | '\u2b45' ..'\u2b46' // Miscellaneous_Symbols_and_Arrows
    | '\u2b4d' ..'\u2b73' // Miscellaneous_Symbols_and_Arrows
    | '\u2b76' ..'\u2b95' // Miscellaneous_Symbols_and_Arrows
    | '\u2b98' ..'\u2bb9' // Miscellaneous_Symbols_and_Arrows
    | '\u2bbd' ..'\u2bc8' // Miscellaneous_Symbols_and_Arrows
    | '\u2bca' ..'\u2bd1' // Miscellaneous_Symbols_and_Arrows
    | '\u2bec' ..'\u2bef' // Miscellaneous_Symbols_and_Arrows
    | '\u2ce5' ..'\u2cea' // Coptic
    | '\u2e80' ..'\u2e99' // CJK_Radicals_Supplement
    | '\u2e9b' ..'\u2ef3' // CJK_Radicals_Supplement
    | '\u2f00' ..'\u2fd5' // Kangxi_Radicals
    | '\u2ff0' ..'\u2ffb' // Ideographic_Description_Characters
    | '\u3004'            // CJK_Symbols_and_Punctuation
    | '\u3012' ..'\u3013' // CJK_Symbols_and_Punctuation
    | '\u3020'            // CJK_Symbols_and_Punctuation
    | '\u3036' ..'\u3037' // CJK_Symbols_and_Punctuation
    | '\u303e' ..'\u303f' // CJK_Symbols_and_Punctuation
    | '\u3190' ..'\u3191' // Kanbun
    | '\u3196' ..'\u319f' // Kanbun
    | '\u31c0' ..'\u31e3' // CJK_Strokes
    | '\u3200' ..'\u321e' // Enclosed_CJK_Letters_and_Months
    | '\u322a' ..'\u3247' // Enclosed_CJK_Letters_and_Months
    | '\u3250'            // Enclosed_CJK_Letters_and_Months
    | '\u3260' ..'\u327f' // Enclosed_CJK_Letters_and_Months
    | '\u328a' ..'\u32b0' // Enclosed_CJK_Letters_and_Months
    | '\u32c0' ..'\u32fe' // Enclosed_CJK_Letters_and_Months
    | '\u3300' ..'\u33ff' // CJK_Compatibility
    | '\u4dc0' ..'\u4dff' // Yijing_Hexagram_Symbols
    | '\ua490' ..'\ua4c6' // Yi_Radicals
    | '\ua828' ..'\ua82b' // Syloti_Nagri
    | '\ua836' ..'\ua837' // Common_Indic_Number_Forms
    | '\ua839'            // Common_Indic_Number_Forms
    | '\uaa77' ..'\uaa79' // Myanmar_Extended-A
    | '\ufdfd'            // Arabic_Presentation_Forms-A
    | '\uffe4'            // Halfwidth_and_Fullwidth_Forms
    | '\uffe8'            // Halfwidth_and_Fullwidth_Forms
    | '\uffed' ..'\uffee' // Halfwidth_and_Fullwidth_Forms
    | '\ufffc' ..'\ufffd' // Specials
;

// mop-up extra characters; these will always create parse errors later, we just can't afford lexer errors
UNMATCHED: .;