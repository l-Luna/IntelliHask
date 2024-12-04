package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.decl.HsDecl;

import java.util.Optional;

public class HsTopDecl extends HsAstElement{
	
	public HsTopDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<HsDecl> inner(){
		return Optional.ofNullable(findChildByClass(HsDecl.class));
	}
}