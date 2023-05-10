import com.riicarus.comandante.main.CommandLogger;

import java.io.IOException;
import java.util.List;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 15:41
 * @since 1.0.0
 */
public class CodeCompiler {

    private final CodeIOHandler ioHandler = new CodeIOHandler();
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(ioHandler);
    private final GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer(ioHandler);

    public void compile(String srcPath, String dstPath) {
        char[] buffer;
        try {
            ioHandler.setPath(srcPath, dstPath);
            buffer = ioHandler.readCodeFromFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<LexicalToken> tokens = doLexicalAnalyze(buffer);

        try {
            doGrammarAnalyze(tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LexicalToken> doLexicalAnalyze(char[] buffer) {
        lexicalAnalyzer.input(buffer);
        return lexicalAnalyzer.doAnalyze();
    }

    private void doGrammarAnalyze(List<LexicalToken> tokens) throws IOException {
        if (ioHandler.checkErrFile()) {
            try {
                grammarAnalyzer.input(tokens);
                grammarAnalyzer.doAnalyze();

                for (GrammarProcess process : SymbolManager.getPROCESSES()) {
                    ioHandler.appendPro(process.toString());
                    ioHandler.appendPro("\r\n");
                }

                for (GrammarVariable variable : SymbolManager.getVARIABLES()) {
                    ioHandler.appendVar(variable.toString());
                    ioHandler.appendVar("\r\n");
                }

                for (LexicalToken token : tokens) {
                    ioHandler.appendDys(token.toString());
                    ioHandler.appendDys("\r\n");
                }

                ioHandler.flushForGrammar();
            } catch (GrammarException e) {
                ioHandler.appendErr(e.getMessage());
                CommandLogger.log(e.getMessage());
            }
        }
    }
}
