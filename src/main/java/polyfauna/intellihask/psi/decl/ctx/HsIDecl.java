package polyfauna.intellihask.psi.decl.ctx;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.decl.HsDecl;

import java.util.Optional;

public class HsIDecl extends HsAstElement{
	
	public HsIDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<HsDecl> inner(){
		return getChildOfType(HsDecl.class);
	}
}