package polyfauna.intellihask.annotators.ext;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ex.ExternalAnnotatorBatchInspection;

public class GhcErrorsInspection extends LocalInspectionTool implements ExternalAnnotatorBatchInspection{

	public static final String SHORT_NAME = "GhcErrors";
}