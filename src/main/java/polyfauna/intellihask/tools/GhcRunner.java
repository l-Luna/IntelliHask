package polyfauna.intellihask.tools;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.execution.wsl.WslDistributionManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import kotlinx.serialization.json.*;
import polyfauna.intellihask.settings.IhSettings;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class GhcRunner{
	
	public static boolean invoke(List<String> filepaths, Reporter reporter)
			throws IOException, ExecutionException, InterruptedException{
		boolean wsl = IhSettings.getIState().useWsl;
		WSLDistribution distro = wsl ? WslDistributionManager.getInstance().getInstalledDistributions().getFirst() : null;
		
		List<String> params = new ArrayList<>();
		if(wsl)
			appendParam(params, "wsl");
		appendParam(params, "~/.ghcup/bin/ghc");
		appendParam(params, "-fdiagnostics-as-json");
		appendParam(params, "-fno-code");
		
		filepaths.forEach(filepath -> appendParam(params, wsl ? distro.getWslPath(Path.of(filepath)) : filepath));
		
		Process proc = new ProcessBuilder(params).start();
		boolean finished = proc.waitFor(15, TimeUnit.SECONDS);
		if(!finished){
			proc.destroy();
			return false; // no second chances
		}
		String out = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(proc.getErrorStream().readAllBytes())).toString();
		List<String> lines = List.of(out.split("\n"));
		
		for(String line : lines)
			if(line.startsWith("{")){
				JsonObject e = (JsonObject)Json.Default.parseToJsonElement(line);
				if(e.get("span") == null || e.get("span") instanceof JsonNull)
					System.err.println("oh no, " + line);
				JsonObject span = (JsonObject)e.get("span");
				JsonObject start = (JsonObject)span.get("start"), end = (JsonObject)span.get("end");
				
				List<String> parts = ((JsonArray)e.get("message")).stream()
						.map(el -> ((JsonPrimitive)el).getContent())
						.toList();
				String message = String.join(", ", parts);
				
				String file = ((JsonPrimitive)span.get("file")).getContent();
				if(wsl)
					file = WSLUtil.getWindowsPath(file, "/mnt/");
				
				reporter.report(message,
						file,
						Integer.parseInt(((JsonPrimitive)start.get("line")).getContent()),
						Integer.parseInt(((JsonPrimitive)start.get("column")).getContent()),
						Integer.parseInt(((JsonPrimitive)end.get("line")).getContent()),
						Integer.parseInt(((JsonPrimitive)end.get("column")).getContent()),
						((JsonPrimitive)e.get("severity")).getContent());
			}
		
		return true;
	}
	
	@FunctionalInterface
	public interface Reporter{
		void report(String message, String file, int lineStart, int columnStart, int lineEnd, int columnEnd, String severity);
	}
	
	public static final Reporter NO_OP_REPORTER = (_1, _2, _3, _4, _5, _6, _7) -> {};
	
	private static void appendParam(List<String> cmdLine, String parameter){
		if(SystemInfo.isWindows)
			if(parameter.contains("\""))
				parameter = StringUtil.replace(parameter, "\"", "\\\"");
			else if(parameter.isEmpty())
				parameter = "\"\"";
		cmdLine.add(parameter);
	}
}