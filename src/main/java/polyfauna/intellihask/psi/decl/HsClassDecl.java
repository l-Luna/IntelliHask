package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.language.decls.BindingType;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsBindingOwner;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.decl.ctx.HsCDecl;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.util.Optional;
import java.util.stream.Stream;

public class HsClassDecl extends HsAstElement implements HsDecl, HsTyVarBinder, HsDeclGroup{
	
	public HsClassDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<HsTyVar> classVar(){
		return getChildOfType(HsTyVar.class);
	}
	
	public Optional<String> classVarName(){
		return classVar().map(HsTyVar::name);
	}
	
	public Stream<HsDecl> innerDecls(){
		return Stream.concat(
				Stream.of(this),
				getChildrenOfType(HsCDecl.class).stream()
						.flatMap(cd -> cd.inner().stream())
		);
	}
	
	public BindingType bindingType(){
		return BindingType.GROUP;
	}
}