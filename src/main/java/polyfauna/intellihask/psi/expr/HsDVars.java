package polyfauna.intellihask.psi.expr;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

import java.util.List;

public class HsDVars extends HsAstElement{
	
	public HsDVars(@NotNull ASTNode node){
		super(node);
	}
	
	public List<HsDVar> vars(){
		return getChildrenOfType(HsDVar.class);
	}
}