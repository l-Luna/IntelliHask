package polyfauna.intellihask.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import polyfauna.intellihask.Util;
import polyfauna.intellihask.psi.HsTyVarBinder;
import polyfauna.intellihask.psi.symbol.TyVarSymbol;
import polyfauna.intellihask.psi.type.HsTyVar;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TyVarColourAnnotator implements Annotator, DumbAware{
	
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder){
		if(!(element instanceof HsTyVarBinder binder))
			return;
		// if(!binder.topLevel()) return;
		List<HsTyVar> vars = new ArrayList<>(binder.findNestedTyVars());
		SortedMap<TyVarSymbol, List<HsTyVar>> sortedVars = new TreeMap<>(Comparator
				.<TyVarSymbol>comparingInt(x -> x.owner().getTextOffset())
				.             thenComparing(TyVarSymbol::name));
		for(HsTyVar tyVar : vars)
			for(TyVarSymbol symb : tyVar.resolveReference())
				sortedVars.merge(symb, Util.mutListOf(tyVar), Util::mergeLLeft);
		int idx = 1;
		for(List<HsTyVar> entry : sortedVars.values()){
			for(HsTyVar tyVar : entry){
				TextAttributes colours = new TextAttributes();
				colours.setForegroundColor(Color.getHSBColor(0.1f * idx, 0.7f, 0.9f));
				holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
						.range(tyVar)
						.enforcedTextAttributes(colours)
						.create();
			}
			idx++;
		}
	}
}