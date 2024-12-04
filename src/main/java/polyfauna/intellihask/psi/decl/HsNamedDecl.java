package polyfauna.intellihask.psi.decl;

import polyfauna.intellihask.psi.expr.HsVar;

import java.util.List;

public interface HsNamedDecl extends HsDecl{
	
	List<HsVar> vars();
	
	default List<String> names(){
		return vars().stream().map(HsVar::name).toList();
	}
}