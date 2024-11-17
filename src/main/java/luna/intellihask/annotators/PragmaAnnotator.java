package luna.intellihask.annotators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import luna.intellihask.language.Extension;
import luna.intellihask.language.Extensions;
import luna.intellihask.parser.HsSyntaxHighlighter;
import luna.intellihask.psi.file.HsModule;
import luna.intellihask.psi.file.HsPragma;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PragmaAnnotator implements Annotator, DumbAware{
	
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder){
		if(element instanceof HsPragma pragma){
			holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
					.textAttributes(HsSyntaxHighlighter.PRAGMA)
					.create();
			
			pragma.asLanguagePragma().ifPresent(lang -> {
				var extElems = lang.extensionNames();
				HsModule module = PsiTreeUtil.getParentOfType(element, HsModule.class);
				Set<Extension> fileExts = module != null ? module.extensions() : Set.of();
				
				for(PsiElement extension : extElems){
					Extension ext = Extensions.EXTENSIONS.get(extension.getText());
					if(ext == null){
						// unknown extensions
						holder.newAnnotation(HighlightSeverity.ERROR, "Unknown extension '" + extension.getText() + "'")
								.range(extension)
								.highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
								.create();
					}else if(Extensions.DEPRECATED_EXTENSIONS.contains(ext)){
						// deprecated extensions
						holder.newAnnotation(HighlightSeverity.WARNING, "Extension '" + ext.name() + "' is deprecated")
								.range(extension)
								.highlightType(ProblemHighlightType.LIKE_DEPRECATED)
								.create();
					}else{
						// check if this extension is implied by any extension in the same file
						var implications = Extensions.IMPLIED_BY.get(ext);
						if(implications != null)
							for(Extension imp : implications)
								if(fileExts.contains(imp))
									holder.newAnnotation(HighlightSeverity.WARNING, "Extension '" + ext.name() + "' is already implied by '" + imp.name() + "'")
											.range(extension)
											.highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
											.create();
					}
				}
			});
		}
	}
}