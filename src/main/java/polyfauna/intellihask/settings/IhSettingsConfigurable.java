package polyfauna.intellihask.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IhSettingsConfigurable implements Configurable{
	
	private JCheckBox useWslButton;
	private JPanel panel;
	
	private JPanel create(IhSettings.IhSetting setting){
		useWslButton = new JCheckBox("Use WSL", setting.useWsl);
		panel = new JPanel();
		panel.add(useWslButton);
		return panel;
	}
	
	public @NlsContexts.ConfigurableName String getDisplayName(){
		return "IntelliHask";
	}
	
	public @Nullable JComponent createComponent(){
		return panel != null ? panel : create(IhSettings.getIState());
	}
	
	public boolean isModified(){
		return IhSettings.getIState().useWsl != useWslButton.isSelected();
	}
	
	public void apply(){
		IhSettings.getIState().useWsl = useWslButton.isSelected();
	}
}