package polyfauna.intellihask.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "polyfauna.intellihask", storages = @Storage("IhSettings.xml"))
public class IhSettings implements PersistentStateComponent<IhSettings.IhSetting>{
	
	public static class IhSetting{
		public boolean useWsl;
	}
	
	private IhSetting state = new IhSetting();
	
	public static IhSettings getInstance(){
		return ApplicationManager.getApplication().getService(IhSettings.class);
	}
	
	public static IhSetting getIState(){
		return getInstance().getState();
	}
	
	public @Nullable IhSettings.IhSetting getState(){
		return state;
	}
	
	public void loadState(@NotNull IhSetting state){
		this.state = state;
	}
}