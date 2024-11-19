package polyfauna.intellihask.language.type;

import java.util.List;

public sealed interface Type{
	
	record TyApp(Type head, List<Type> args) implements Type{}
	
	enum Arrow implements Type{ ARROW }
	enum List_ implements Type{ LIST }
	record Tuple(int elements) implements Type{}
	record TyVar(String name) implements Type{}
	record NamedTyCtor(String name) implements Type{}
	
	default String pretty(){
		return switch(this){
			case TyApp(Type head, List<Type> args) when head == Arrow.ARROW && args.size() == 2
					-> args.get(0).pretty() + " -> " + args.get(1).pretty();
			case TyApp(Type head, List<Type> args) when head == List_.LIST && args.size() == 1
					-> "[" + args.getFirst().pretty() + "]";
			case TyApp(Tuple(int count), List<Type> args) when args.size() == count
					-> "(" + String.join(", ", args.stream().map(Type::pretty).toList()) + ")";
			
			case TyApp(Type head, List<Type> args) -> head.pretty() + String.join(" ", args.stream().map(Type::pretty).toList());
			case Arrow.ARROW -> "(->)";
			case List_.LIST -> "[]";
			case Tuple(int count) -> "(" + ",".repeat(Math.max(0, count - 1)) + ")";
			case TyVar(String name) -> name;
			case NamedTyCtor(String name) -> name;
		};
	}
}