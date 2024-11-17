package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;

public class HsModuleBody extends HsAstElement{
	
	public HsModuleBody(@NotNull ASTNode node){
		super(node);
	}
}