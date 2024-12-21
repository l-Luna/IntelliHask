package polyfauna.intellihask.psi.pattern;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;

public class HsPattern extends HsAstElement{
	
	public HsPattern(@NotNull ASTNode node){
		super(node);
	}
}