package luna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import luna.intellihask.antlr_generated.HaskellParser;
import luna.intellihask.language.Extension;
import luna.intellihask.language.Extensions;
import luna.intellihask.psi.HsAstElement;
import luna.intellihask.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HsLanguagePragma extends HsAstElement{
	
	public HsLanguagePragma(@NotNull ASTNode node){
		super(node);
	}
	
	public List<PsiElement> extensionNames(){
		List<PsiElement> ret = new ArrayList<>();
		for(PsiElement child : getChildren())
			if(child.getNode().getElementType() == Tokens.getRuleFor(HaskellParser.RULE_extension_))
				ret.add(child);
		return ret;
	}
	
	public Set<Extension> extensions(){
		return extensionNames().stream()
				.map(PsiElement::getText)
				.map(Extensions.EXTENSIONS::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}
}