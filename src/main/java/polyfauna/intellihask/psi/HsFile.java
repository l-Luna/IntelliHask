package polyfauna.intellihask.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import polyfauna.intellihask.HaskellFileType;
import polyfauna.intellihask.HaskellLanguage;
import polyfauna.intellihask.psi.file.HsModule;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HsFile extends PsiFileBase{
	
	public HsFile(@NotNull FileViewProvider viewProvider){
		super(viewProvider, HaskellLanguage.INSTANCE);
	}
	
	public static boolean isHsFile(PsiFile file){
		return file instanceof HsFile || file.getLanguage().isKindOf(HaskellLanguage.INSTANCE);
	}
	
	public @NotNull FileType getFileType(){
		return HaskellFileType.INSTANCE;
	}
	
	public Optional<HsModule> module(){
		return Optional.ofNullable(PsiTreeUtil.findChildOfType(this, HsModule.class));
	}
}