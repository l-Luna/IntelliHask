package polyfauna.intellihask.annotators.ext;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.psi.HsFile;

import java.nio.file.Path;
import java.util.List;

// see https://github.com/l-Luna/CyclicIntellijPlugin/blob/master/src/main/java/cyclic/intellij/presentation/compilerLints/CompilerExternalAnnotator.java
public class GhcExternalAnnotator extends ExternalAnnotator<PsiFile, List<GhcError>>{
	
	public @Nullable PsiFile collectInformation(@NotNull PsiFile file){
		return HsFile.isHsFile(file) ? file : null;
	}
	
	public @Nullable List<GhcError> doAnnotate(PsiFile info){
		if(info == null)
			return null;
		
		return GhcErrorsManager.getProblems(info.getProject());
	}
	
	public @Nullable String getPairedBatchInspectionShortName(){
		return GhcErrorsInspection.SHORT_NAME;
	}
	
	public void apply(@NotNull PsiFile file, List<GhcError> errors, @NotNull AnnotationHolder holder){
		if(errors == null || errors.isEmpty())
			return;
		
		for(GhcError error : errors){
			if(Path.of(file.getVirtualFile().getCanonicalPath()).equals(Path.of(error.file()))){
				String body = file.getText();
				if(error.lineStart() >= 0){
					holder.newAnnotation(HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING, "")
							.tooltip(error.message().replace("\n", "<br />"))
							.range(TextRange.create(
									charInLineToOffset(error.lineStart(), error.columnStart(), body),
									charInLineToOffset(error.lineEnd(), error.columnEnd(), body))
							).create();
				}
			}
		}
	}
	
	// stupid way of doing it -past luna
	// present day luna thinks this is Good Enough
	private static int charInLineToOffset(int line, int column, String text){
		int numNewlines = line - 1;
		int passed = 0;
		while(numNewlines > 0 && text.length() > passed){
			if(text.charAt(passed) == '\n')
				numNewlines--;
			passed++;
		}
		return passed + column - 1;
	}
}