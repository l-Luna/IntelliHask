package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.decl.ctx.HsCDecl;
import polyfauna.intellihask.psi.expr.HsVar;
import polyfauna.intellihask.psi.expr.HsVars;
import polyfauna.intellihask.psi.type.HsType;

import java.util.List;
import java.util.Optional;

public class HsVarTypeDecl extends HsAstElement implements HsTyVarBinder, HsNamedDecl{
	
	public HsVarTypeDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public List<String> names(){
		return vars().stream().map(HsVar::name).toList();
	}
	
	public List<HsVar> vars(){
		return getChildOfType(HsVars.class).map(HsVars::vars).orElse(List.of());
	}
	
	public Optional<HsType> type(){
		return Optional.ofNullable(PsiTreeUtil.getChildOfType(this, HsType.class));
	}
	
	public boolean captures(String name){
		// bindings in class or instance declarations let references to the class type fall through
		// TODO: more general lexical scoping (inc. in findNestedTyVars())
		if(getParent() instanceof HsCDecl cd && cd.getParent() instanceof HsClassDecl cld)
			if(cld.classVarName().equals(Optional.of(name)))
				return false;
		return HsTyVarBinder.super.captures(name);
	}
}