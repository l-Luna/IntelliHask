package polyfauna.intellihask.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.DumbAware;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import polyfauna.intellihask.language.extensions.Extension;
import polyfauna.intellihask.language.extensions.Extensions;
import polyfauna.intellihask.psi.Tokens;
import org.jetbrains.annotations.NotNull;

public class ExtCompletionContributor extends CompletionContributor implements DumbAware{

	public ExtCompletionContributor(){
		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(Tokens.getRuleFor(HaskellParser.RULE_extension_))),
				new CompletionProvider<>(){
					protected void addCompletions(@NotNull CompletionParameters params, @NotNull ProcessingContext ctx, @NotNull CompletionResultSet result){
						for(Extension ext : Extensions.EXTENSIONS.values()){
							LookupElementBuilder builder = LookupElementBuilder.create(ext.name());
							if(Extensions.DEPRECATED_EXTENSIONS.contains(ext))
								builder = builder.strikeout();
							// TODO: show implied extensions on the side
							result.addElement(PrioritizedLookupElement.withPriority(builder, 1));
							result.addElement(LookupElementBuilder.create("No" + ext.name()));
						}
					}
				});
	}
}