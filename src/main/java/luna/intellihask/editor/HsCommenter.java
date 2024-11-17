package luna.intellihask.editor;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

public class HsCommenter implements Commenter{
	
	public @Nullable String getLineCommentPrefix(){
		return "--";
	}
	
	public @Nullable String getBlockCommentPrefix(){
		return "{-";
	}
	
	public @Nullable String getBlockCommentSuffix(){
		return "-}";
	}
	
	public @Nullable String getCommentedBlockCommentPrefix(){
		return null;
	}
	
	public @Nullable String getCommentedBlockCommentSuffix(){
		return null;
	}
}