package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.type.HsType;

import java.util.Optional;

public class HsVarTypeDecl extends HsAstElement implements HsTyVarBinder{
	
	public HsVarTypeDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<HsType> type(){
		return Optional.ofNullable(PsiTreeUtil.getChildOfType(this, HsType.class));
	}
}