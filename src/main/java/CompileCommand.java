import com.riicarus.comandante.main.CommandLauncher;

import java.util.LinkedList;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 15:44
 * @since 1.0.0
 */
public class CompileCommand {

    public static CodeCompiler codeCompiler = new CodeCompiler();

    @SuppressWarnings("all")
    public static void defineCommand() {
        CommandLauncher.register().builder()
                .main("compiler")
                .opt("compile", "c")
                .arg("srcPath")
                .arg("dstPath")
                .executor(
                        (args, pipedArg) -> {
                            codeCompiler.compile(((LinkedList<String>)args).get(0), ((LinkedList<String>)args).get(1));
                            return "";
                        }
                );
    }

}
