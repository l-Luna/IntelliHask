package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.language.extensions.Extension;
import polyfauna.intellihask.language.extensions.Toggle;
import polyfauna.intellihask.psi.HsAstElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.decl.HsDecl;

import java.util.List;
import java.util.Optional;
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
	
	public Optional<HsModuleBody> body(){
		return Optional.ofNullable(findChildByClass(HsModuleBody.class));
	}
	
	public List<HsDecl> decls(){
		return body().map(HsModuleBody::decls).orElse(List.of());
	}
}