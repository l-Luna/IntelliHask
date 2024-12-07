package polyfauna.intellihask.psi.decl;

import com.intellij.psi.PsiElement;

import java.util.stream.Stream;

public interface HsDecl extends PsiElement{

	// or a default, fixity...
	
	default Stream<HsDecl> innerDecls(){
		return Stream.of(this);
	}
}