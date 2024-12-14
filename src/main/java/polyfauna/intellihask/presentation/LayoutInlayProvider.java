package polyfauna.intellihask.presentation;

import com.intellij.codeInsight.hints.declarative.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.HsFile;
import polyfauna.intellihask.psi.Tokens;

import java.util.List;

public class LayoutInlayProvider implements InlayHintsProvider, DumbAware{
	
	public @Nullable InlayHintsCollector createCollector(@NotNull PsiFile file, @NotNull Editor editor){
		return !HsFile.isHsFile(file) ? null : new LayoutInlayCollector();
	}
	
	private static class LayoutInlayCollector implements SharedBypassCollector{
		
		public void collectFromElement(@NotNull PsiElement element, @NotNull InlayTreeSink sink){
			if(element instanceof HsAstElement hae){
				var elemType = hae.getNode().getElementType();
				boolean isOpen, isClose = false;
				if((isOpen = elemType == Tokens.getRuleFor(HaskellParser.RULE_open_))
				|| (isClose = elemType == Tokens.getRuleFor(HaskellParser.RULE_close))
				|| (elemType == Tokens.getRuleFor(HaskellParser.RULE_semi))){
					if(element.getTextLength() == 0){
						boolean fo = isOpen, fc = isClose;
						sink.addPresentation(
								new InlineInlayPosition(element.getTextRange().getEndOffset(), true, 0),
								List.of(),
								null,
								HintFormat.Companion.getDefault().withFontSize(HintFontSize.ABitSmallerThanInEditor),
								builder -> {
									builder.text(fo ? "{" : fc ? "}" : ";", null);
									return Unit.INSTANCE;
								}
						);
					}
				}
			}
		}
	}
}