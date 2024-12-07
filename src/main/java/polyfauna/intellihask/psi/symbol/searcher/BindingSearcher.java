package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import polyfauna.intellihask.psi.HsFile;
import polyfauna.intellihask.psi.decl.HsDecl;
import polyfauna.intellihask.psi.decl.HsNamedDecl;
import polyfauna.intellihask.psi.symbol.BindingSymbol;
import polyfauna.intellihask.psi.symbol.PsiBindingSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class BindingSearcher{

	// ...from a read action
	public static Stream<BindingSymbol> all(Project project){
		List<VirtualFile> sources = new ArrayList<>();
		ProjectRootManager.getInstance(project).getFileIndex().iterateContent(file -> {
			sources.add(file);
			return true;
		}, file -> Objects.equals(file.getExtension(), "hs"));
		PsiManager pm = PsiManager.getInstance(project);
		return sources.stream()
				.map(pm::findFile)
				.filter(HsFile.class::isInstance)
				.map(HsFile.class::cast)
				.flatMap(hf -> hf.module().stream())
				.flatMap(mod -> mod.decls().stream()
						.flatMap(HsDecl::innerDecls)
						.filter(HsNamedDecl.class::isInstance)
						.flatMap(decl -> ((HsNamedDecl)decl).names().stream())
						.map(decl -> new PsiBindingSymbol(decl, mod)));
	}
	
	public static Stream<BindingSymbol> byName(Project project, String name){
		return all(project).filter(b -> b.name().equals(name));
	}
}