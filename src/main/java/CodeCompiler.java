import com.riicarus.comandante.main.CommandLogger;

import java.io.IOException;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 15:41
 * @since 1.0.0
 */
public class CodeCompiler {

    private final CodeIOHandler ioHandler = new CodeIOHandler();
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    public void compile(String srcPath, String dstPath) {
        char[] buffer;
        try {
            ioHandler.setPath(srcPath, dstPath);
            buffer = ioHandler.readCodeFromFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        lexicalAnalyzer.input(buffer);
        try {
            try {
                while (true) {
                    final LexicalToken token = lexicalAnalyzer.analyzeOne();
                    if (token != null) {
                        ioHandler.appendDyd(token.toString());
                        ioHandler.appendDyd("\r\n");
                    } else {
                        break;
                    }
                }
            } catch (LexicalException e) {
                CommandLogger.log(e.getMessage());
                ioHandler.appendErr(e.getMessage());
            }

            ioHandler.flushForLexical();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
