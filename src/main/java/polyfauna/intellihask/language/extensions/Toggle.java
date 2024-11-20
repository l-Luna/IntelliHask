package polyfauna.intellihask.language.extensions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record Toggle(Extension extension, boolean on){
	
	public String name(){
		return (!on ? "No" : "") + extension.name();
	}
	
	public static Optional<Toggle> parse(String text){
		if(text.startsWith("No"))
			return Optional.ofNullable(Extensions.EXTENSIONS.get(text.substring(2))).map(Extension::off);
		return Optional.ofNullable(Extensions.EXTENSIONS.get(text)).map(Extension::on);
	}
	
	/**
	 * Resolve a list of explicit extension toggles to a set of enabled extensions, introducing
	 * implied extensions.
	 * <p>
	 * Note that the order of extension toggles matters; latter toggles always take precedence.
	 * Implicit extensions should therefore be added to the start of the list.
	 */
	public static Set<Extension> resolve(List<Toggle> toggles){
		Set<Extension> ret = new HashSet<>();
		for(Toggle toggle : toggles){
			if(!toggle.on)
				ret.remove(toggle.extension);
			else{
				ret.add(toggle.extension);
				if(Extensions.IMPLIES.containsKey(toggle.extension)){
					// implications are only considered one level deep; this is handled by the shape of implications,
					// and required for proper handling of GHC2021
					for(Toggle implication : Extensions.IMPLIES.get(toggle.extension))
						if(implication.on)
							ret.add(implication.extension);
						else
							ret.remove(implication.extension);
				}
			}
		}
		return ret;
	}
}