import com.riicarus.comandante.main.CommandLauncher;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 15:44
 * @since 1.0.0
 */
public class CompileCommand {

    public static CodeCompiler codeCompiler = new CodeCompiler();

    public static void defineCommand() {
        CommandLauncher.register().builder()
                .main("compiler")
                .opt("compile", "c")
                .arg("path")
                .executor(
                        (args, pipedArg) -> {
                            System.out.println(args.toString());
                            return "";
                        }
                );
    }

}
