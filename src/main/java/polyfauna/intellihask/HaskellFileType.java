package polyfauna.intellihask;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HaskellFileType extends LanguageFileType{
	
	public static final HaskellFileType INSTANCE = new HaskellFileType();
	
	protected HaskellFileType(){
		super(HaskellLanguage.INSTANCE);
	}
	
	public @NonNls @NotNull String getName(){
		return "Haskell File";
	}
	
	public @NlsContexts.Label @NotNull String getDescription(){
		return "Haskell file";
	}
	
	public @NlsSafe @NotNull String getDefaultExtension(){
		return "hs";
	}
	
	public Icon getIcon(){
		return AllIcons.FileTypes.Text; // TODO
	}
}