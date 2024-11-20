package polyfauna.intellihask.psi.file;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import polyfauna.intellihask.antlr_generated.HaskellParser;
import polyfauna.intellihask.language.extensions.Toggle;
import polyfauna.intellihask.psi.HsAstElement;
import polyfauna.intellihask.psi.Tokens;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
	
	public Set<Toggle> extensions(){
		return extensionNames().stream()
				.map(PsiElement::getText)
				.flatMap(x -> Toggle.parse(x).stream())
				.collect(Collectors.toSet());
	}
}