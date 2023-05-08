import com.riicarus.comandante.main.CommandLauncher;
import com.riicarus.comandante.manage.ConsoleIOListener;

import java.io.File;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 16:05
 * @since 1.0.0
 */
public class TestMain {

    public static void main(String[] args) {
        CodeCompiler codeCompiler = new CodeCompiler();
        File file = new File("");
        String absPath = file.getAbsolutePath();
        codeCompiler.compile(absPath + "\\code.pas", absPath);

        CompileCommand.defineCommand();
        CommandLauncher.enable();
        Thread thread = new Thread(new ConsoleIOListener());
        thread.start();
    }

}
