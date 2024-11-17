package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HsPragma extends HsAstElement{
	
	public HsPragma(@NotNull ASTNode node){
		super(node);
	}
	
	public Optional<HsLanguagePragma> asLanguagePragma(){
		return Optional.ofNullable(PsiTreeUtil.getChildOfType(this, HsLanguagePragma.class));
	}
}