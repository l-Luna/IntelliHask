package polyfauna.intellihask.annotators.ext;

public record GhcError(String message, String file, int lineStart, int columnStart, int lineEnd, int columnEnd, String severity){

}