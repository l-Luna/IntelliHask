package polyfauna.intellihask.psi.symbol.searcher;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import polyfauna.intellihask.psi.HsFile;
import polyfauna.intellihask.psi.decl.HsDecl;
import polyfauna.intellihask.psi.decl.HsDeclGroup;
import polyfauna.intellihask.psi.decl.HsNamedDecl;
import polyfauna.intellihask.psi.symbol.BindingSymbol;
import polyfauna.intellihask.psi.symbol.PsiGroupedBindingSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class BindingSearcher{

	public static Stream<BindingSymbol> inGroup(HsDeclGroup group){
		return group.decls().stream()
				.flatMap(HsDecl::innerDecls)
				.filter(HsNamedDecl.class::isInstance)
				.flatMap(decl -> ((HsNamedDecl)decl).names().stream())
				.map(decl -> new PsiGroupedBindingSymbol(decl, group));
	}
	
	// ...from a read action
	public static Stream<BindingSymbol> topLevel(Project project){
		List<VirtualFile> sources = new ArrayList<>();
		ProjectRootManager.getInstance(project).getFileIndex().iterateContent(file -> {
			sources.add(file);
			return true;
		}, file -> Objects.equals(file.getExtension(), "hs"));
		PsiManager pm = PsiManager.getInstance(project);
		return sources.stream()
				.map(pm::findFile)
				.filter(HsFile.class::isInstance)
				.flatMap(hf -> ((HsFile)hf).module().stream())
				.flatMap(mod -> mod.body().stream())
				.flatMap(BindingSearcher::inGroup);
	}
	
	public static Stream<BindingSymbol> topLevelByName(Project project, String name){
		return topLevel(project).filter(b -> b.name().equals(name));
	}
}