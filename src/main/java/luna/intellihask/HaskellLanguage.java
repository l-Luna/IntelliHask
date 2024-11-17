package luna.intellihask;

import com.intellij.lang.Language;

public class HaskellLanguage extends Language{
	
	public static final HaskellLanguage INSTANCE = new HaskellLanguage();
	
	protected HaskellLanguage(){
		super("Haskell");
	}
}