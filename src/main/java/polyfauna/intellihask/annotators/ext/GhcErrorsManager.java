package polyfauna.intellihask.annotators.ext;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiModificationTracker;
import polyfauna.intellihask.tools.GhcRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GhcErrorsManager{

	private static long lastModificationCount;
	private static List<GhcError> lastErrors;
	
	public static synchronized List<GhcError> getProblems(Project project){
		PsiModificationTracker tracker = PsiModificationTracker.getInstance(project);
		if(tracker.getModificationCount() == lastModificationCount)
			return lastErrors;
		
		ApplicationManager.getApplication().invokeAndWait(() -> ((FileDocumentManagerImpl)FileDocumentManager.getInstance()).saveAllDocuments(false));
		long currentModificationCount = tracker.getModificationCount();
		
		List<String> paths = new ArrayList<>();
		for(Module module : ModuleManager.getInstance(project).getModules()){
			for(VirtualFile root : ModuleRootManager.getInstance(module).getSourceRoots()){
				VfsUtil.iterateChildrenRecursively(root, __ -> true, (VirtualFile vf) -> {
					if(Objects.equals(vf.getExtension(), "hs"))
						paths.add(vf.getCanonicalPath());
					return true;
				});
			}
		}
		
		List<GhcError> currentErrors = new ArrayList<>();
		if(!paths.isEmpty())
			try{
				boolean result = GhcRunner.invoke(paths, (m, f, ls, cs, le, ce, sev) -> currentErrors.add(new GhcError(m, f, ls, cs, le, ce, sev)));
			}catch(IOException | ExecutionException | InterruptedException e){
				throw new RuntimeException(e);
			}
		
		lastModificationCount = currentModificationCount;
		lastErrors = currentErrors;
		
		return lastErrors;
	}
}