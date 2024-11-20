package polyfauna.intellihask.annotators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.language.extensions.Extension;
import polyfauna.intellihask.language.extensions.Extensions;
import polyfauna.intellihask.language.extensions.Toggle;
import polyfauna.intellihask.parser.HsSyntaxHighlighter;
import polyfauna.intellihask.psi.file.HsModule;
import polyfauna.intellihask.psi.file.HsPragma;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PragmaAnnotator implements Annotator, DumbAware{
	
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder){
		if(element instanceof HsPragma pragma){
			holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
					.textAttributes(HsSyntaxHighlighter.PRAGMA)
					.create();
			
			pragma.asLanguagePragma().ifPresent(lang -> {
				List<PsiElement> extElems = lang.extensionNames();
				HsModule module = PsiTreeUtil.getParentOfType(element, HsModule.class);
				Set<Extension> fileExts = module != null ? module.extensions() : Set.of();
				
				for(PsiElement extension : extElems){
					Optional<Toggle> toggleO = Toggle.parse(extension.getText());
					if(toggleO.isEmpty()){
						// unknown extensions
						holder.newAnnotation(HighlightSeverity.ERROR, "Unknown extension '" + extension.getText() + "'")
								.range(extension)
								.highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
								.create();
					}else{
						Toggle toggle = toggleO.get();
						if(toggle.on() && Extensions.DEPRECATED_EXTENSIONS.contains(toggle.extension())){
							// deprecated extensions
							holder.newAnnotation(HighlightSeverity.WARNING, "Extension '" + toggle.name() + "' is deprecated")
									.range(extension)
									.highlightType(ProblemHighlightType.LIKE_DEPRECATED)
									.create();
						}else{
							// check if this extension is implied by any extension in the same file
							var implications = Extensions.IMPLIED_BY.get(toggle);
							if(implications != null)
								for(Extension imp : implications)
									if(fileExts.contains(imp))
										holder.newAnnotation(HighlightSeverity.WARNING, "Extension '" + toggle.name() + "' is already implied by '" + imp.name() + "'")
												.range(extension)
												.highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
												.create();
						}
					}
				}
			});
		}
	}
}