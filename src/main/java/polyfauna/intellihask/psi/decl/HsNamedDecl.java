package polyfauna.intellihask.psi.decl;

import polyfauna.intellihask.psi.expr.HsDVar;

import java.util.List;

public interface HsNamedDecl extends HsDecl{
	
	List<HsDVar> vars();
	
	default List<String> names(){
		return vars().stream().map(HsDVar::name).toList();
	}
}