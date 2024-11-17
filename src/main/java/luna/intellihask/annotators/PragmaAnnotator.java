package luna.intellihask.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import luna.intellihask.parser.HsSyntaxHighlighter;
import luna.intellihask.psi.HsPragma;
import org.jetbrains.annotations.NotNull;

public class PragmaAnnotator implements Annotator, DumbAware{
	
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder){
		if(element instanceof HsPragma)
			holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
					.textAttributes(HsSyntaxHighlighter.PRAGMA)
					.create();
	}
}