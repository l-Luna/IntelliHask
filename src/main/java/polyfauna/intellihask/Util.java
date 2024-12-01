package polyfauna.intellihask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
	
	public static <T, U> Optional<List<U>> traverse(List<T> ls, Function<T, Optional<U>> f){
		List<U> parts = new ArrayList<>(ls.size());
		for(T t : ls){
			var l = f.apply(t);
			if(l.isPresent())
				parts.add(l.get());
			else return Optional.empty();
		}
		return Optional.of(parts);
	}
}