package luna.intellihask.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import luna.intellihask.HaskellFileType;
import luna.intellihask.HaskellLanguage;
import org.jetbrains.annotations.NotNull;

public class HsFile extends PsiFileBase{
	
	public HsFile(@NotNull FileViewProvider viewProvider){
		super(viewProvider, HaskellLanguage.INSTANCE);
	}
	
	public @NotNull FileType getFileType(){
		return HaskellFileType.INSTANCE;
	}
}