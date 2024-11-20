package polyfauna.intellihask;

import java.util.ArrayList;
import java.util.List;

public final class Util{
	
	private Util(){}
	
	@SafeVarargs
	public static <T> List<T> mutListOf(T... ts){
		return new ArrayList<>(List.of(ts));
	}
	
	// for use in Map::merge
	public static <T> List<T> mergeLLeft(List<T> left, List<T> right){
		left.addAll(right);
		return left;
	}
}