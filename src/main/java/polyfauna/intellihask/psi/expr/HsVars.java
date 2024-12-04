package polyfauna.intellihask.psi.expr;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

import java.util.List;

public class HsVars extends HsAstElement{
	
	public HsVars(@NotNull ASTNode node){
		super(node);
	}
	
	public List<HsVar> vars(){
		return getChildrenOfType(HsVar.class);
	}
}