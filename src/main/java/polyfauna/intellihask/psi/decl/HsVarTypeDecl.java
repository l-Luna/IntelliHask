package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.expr.HsVar;
import polyfauna.intellihask.psi.expr.HsVars;
import polyfauna.intellihask.psi.type.HsType;

import java.util.List;
import java.util.Optional;

public class HsVarTypeDecl extends HsAstElement implements HsTyVarBinder, HsNamedDecl{
	
	public HsVarTypeDecl(@NotNull ASTNode node){
		super(node);
	}
	
	public List<String> names(){
		return vars().stream().map(HsVar::name).toList();
	}
	
	public List<HsVar> vars(){
		return getChildOfType(HsVars.class).map(HsVars::vars).orElse(List.of());
	}
	
	public Optional<HsType> type(){
		return Optional.ofNullable(PsiTreeUtil.getChildOfType(this, HsType.class));
	}
	
	/*public @NotNull Pointer<? extends DocumentationTarget> createPointer(){
		return SmartPointerManager.createPointer(this);
	}
	
	public @NotNull TargetPresentation computePresentation(){
		return TargetPresentation.builder("").presentation();
	}
	
	public @Nullable DocumentationResult computeDocumentation(){
		return DocumentationResult.documentation("binding %s<b>%s</b> :: %s %s"
				.formatted("<samp>", "..", type().flatMap(HsType::rep).map(Type::pretty).orElse("(invalid)"), "</samp>"));
	}*/
}