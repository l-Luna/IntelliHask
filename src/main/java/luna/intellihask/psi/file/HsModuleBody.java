package luna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import luna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;

public class HsModuleBody extends HsAstElement{
	
	public HsModuleBody(@NotNull ASTNode node){
		super(node);
	}
}