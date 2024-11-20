package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.language.extensions.Extension;
import polyfauna.intellihask.language.extensions.Toggle;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class HsModule extends HsAstElement{
	
	public HsModule(@NotNull ASTNode node){
		super(node);
	}
	
	public List<HsPragma> pragmas(){
		return PsiTreeUtil.getChildrenOfTypeAsList(this, HsPragma.class);
	}
	
	public Set<Extension> extensions(){
		return Toggle.resolve(pragmas().stream()
				.flatMap(x -> x.asLanguagePragma().stream())
				.flatMap(x -> x.extensions().stream())
				.toList());
	}
}