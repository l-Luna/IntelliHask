package polyfauna.intellihask.psi.decl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.model.Pointer;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.language.type.Type;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.type.HsType;

import java.util.Optional;

public class HsVarTypeDecl extends HsAstElement implements HsTyVarBinder, HsDecl{
	
	public HsVarTypeDecl(@NotNull ASTNode node){
		super(node);
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