package luna.intellihask.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.tree.IElementType;
import luna.intellihask.HaskellLanguage;
import luna.intellihask.antlr_generated.HaskellParser;
import org.antlr.intellij.adaptor.lexer.PSITokenSource;
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter;
import org.antlr.intellij.adaptor.parser.ErrorStrategyAdaptor;
import org.antlr.intellij.adaptor.parser.SyntaxErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jetbrains.annotations.NotNull;

// adapted (heavily) from ANTLRParserAdaptor
// and inspired by the python plugin's impl: https://github.com/JetBrains/intellij-community/blob/master/python/python-parser/src/com/jetbrains/python/parsing/PythonParser.java
// insert indentation tokens as necessary for parsing purposes, but don't include them in the actual tree
public class ParserAdapter implements PsiParser{
	
	protected final Parser parser;
	
	public ParserAdapter(){
		parser = new HaskellParser(null);
	}
	
	public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder){
		TokenSource source = new LyingTokenSource(builder);
		TokenStream tokens = new CommonTokenStream(source);
		parser.setTokenStream(tokens);
		parser.setErrorHandler(new ErrorStrategyAdaptor()); // tweaks missing tokens
		parser.removeErrorListeners();
		parser.addErrorListener(new SyntaxErrorListener()); // trap errors
		ParseTree parseTree;
		PsiBuilder.Marker rollbackMarker = builder.mark();
		try{
			parseTree = parseA(parser, root);
		}finally{
			rollbackMarker.rollbackTo();
		}
		
		// Now convert ANTLR parser tree to PSI tree by mimicking subtree
		// enter/exit with mark/done calls. I *think* this creates their parse
		// tree (AST as they call it) when you call {@link PsiBuilder#getTreeBuilt}
		ANTLRParseTreeToPSIConverter listener = new LyingTreeConverter(HaskellLanguage.INSTANCE, parser, builder);
		PsiBuilder.Marker rootMarker = builder.mark();
		ParseTreeWalker.DEFAULT.walk(listener, parseTree);
		while(!builder.eof()){ // mop up remaining chars; this appears as nonsense on the PSI tree window though, avoid this!
			ProgressIndicatorProvider.checkCanceled();
			builder.advanceLexer();
		}
		
		rootMarker.done(root);
		return builder.getTreeBuilt();
	}
	
	protected ParseTree parseA(Parser parser, IElementType root){
		HaskellParser hparser = (HaskellParser)parser;
		// ... indexed things go here ...
		return hparser.module();
	}
}