package polyfauna.intellihask.psi.type;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.Util;
import polyfauna.intellihask.antlr_generated.HaskellLexer;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import polyfauna.intellihask.language.type.Type;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.Tokens;

import java.util.List;
import java.util.Optional;

public class HsType extends HsAstElement{
	
	public HsType(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<Type> rep(){
		return repTy(this);
	}
	
	// not a fan tbh
	private static Optional<Type> repTy(HsAstElement elem){
		List<HsAstElement> btypes = elem.getChildrenOfAstType(Tokens.getRuleFor(HaskellParser.RULE_btype));
		if(btypes.size() == 2 && elem.countChildrenOfAstType(Tokens.getFor(HaskellLexer.Arrow)) == 1)
			return repB(btypes.get(0)).flatMap(l ->
					repB(btypes.get(1)).map(r ->
						new Type.TyApp(Type.Arrow.ARROW, List.of(l, r))));
		if(btypes.size() == 1)
			return repB(btypes.getFirst());
		return Optional.empty();
	}
	
	private static Optional<Type> repB(HsAstElement elem){
		List<HsAstElement> atypes = elem.getChildrenOfAstType(Tokens.getRuleFor(HaskellParser.RULE_atype));
		if(!atypes.isEmpty())
			return Util.traverse(atypes, HsType::repA).map(parts -> {
				if(parts.size() > 1){
					Type head = parts.removeFirst();
					return new Type.TyApp(head, parts);
				}
				return parts.getFirst();
			});
		return Optional.empty();
	}
	
	private static Optional<Type> repA(HsAstElement elem){
		List<HsAstElement> types = elem.getChildrenOfAstType(Tokens.getRuleFor(HaskellParser.RULE_type));
		boolean tup = false, paren = false;
		if(!types.isEmpty()
				&& (tup = elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.Comma)))
				|| (paren = elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.OpenRoundBracket)))
				|| (elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.OpenSquareBracket)))){
			boolean ftup = tup, fparen = paren;
			return Util.traverse(types, HsType::repTy).map(parts -> {
				if(ftup)
					return new Type.TyApp(new Type.Tuple(parts.size()), parts);
				if(fparen)
					return parts.getFirst();
				return new Type.TyApp(Type.List_.LIST, List.of(parts.getFirst()));
			});
		}
		List<HsAstElement> tyvars = elem.getChildrenOfAstType(Tokens.getRuleFor(HaskellParser.RULE_tyvar));
		if(tyvars.size() == 1)
			return Optional.of(new Type.TyVar(tyvars.getFirst().getText()));
		List<HsAstElement> gtycons = elem.getChildrenOfAstType(Tokens.getRuleFor(HaskellParser.RULE_gtycon));
		if(gtycons.size() == 1)
			return repG(gtycons.getFirst());
		return Optional.empty();
	}
	
	private static Optional<Type> repG(HsAstElement elem){
		int tup = elem.countChildrenOfAstType(Tokens.getFor(HaskellLexer.Comma));
		if(tup > 0)
			return Optional.of(new Type.Tuple(tup));
		if(elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.Arrow)))
			return Optional.of(Type.Arrow.ARROW);
		if(elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.OpenSquareBracket)))
			return Optional.of(Type.List_.LIST);
		if(elem.hasChildOfAstType(Tokens.getFor(HaskellLexer.OpenRoundBracket)))
			return Optional.of(new Type.Tuple(0));
		return Optional.of(new Type.NamedTyCtor(elem.getText()));
	}
}