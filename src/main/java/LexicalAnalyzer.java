import com.riicarus.comandante.main.CommandLogger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-7 16:44
 * @since 1.0.0
 */
public class LexicalAnalyzer {

    private final StringBuilder tokenBuilder = new StringBuilder();

    private final CodeIOHandler ioHandler;

    private LexicalToken token;

    private char[] buffer;

    private int idx = 0;

    private int line = 1;

    private char b;

    private boolean ended;

    public LexicalAnalyzer(CodeIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    protected void resetForNextProgram() {
        idx = 0;
        line = 1;
        b = ' ';
        ended = false;

        resetForNextToken();
    }

    protected void resetForNextToken() {
        tokenBuilder.delete(0, tokenBuilder.length());
        token = null;
    }

    public void input(char[] input) {
        resetForNextProgram();
        this.buffer = input;
        this.b = buffer[idx];
    }

    public List<LexicalToken> doAnalyze() {
        List<LexicalToken> tokens = new LinkedList<>();
        try {
            try {
                while (true) {
                    final LexicalToken token = analyzeOne();
                    if (token != null) {
                        ioHandler.appendDyd(token.toString());
                        ioHandler.appendDyd("\r\n");
                        tokens.add(token);
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
            CommandLogger.log("Write Lexical analyze result failed.");
        }

        CommandLogger.log("Lexical analyze succeeded.");

        return tokens;
    }

    protected LexicalToken analyzeOne() throws LexicalException {
        resetForNextToken();

        ignoreBlankSpace();

        if (isEnd()) {
            if (!ended) {
                token = SymbolManager.getReservedToken("EOF");
                ended = true;
            }

            return token;
        }

        if (isLetter()) {
            handleWord();
        } else if (isDigit()) {
            handleConstant();
        } else if (isSingleSymbol()) {
            handleSingleSymbol();
        } else if (isMultiSymbol()) {
            handleMultiSymbol();
        } else if (isEOLN()) {
            handleEOLN();
        } else {
            throw new LexicalException("***LINE:" + line + "  Unexpected identifier.");
        }

        return token;
    }

    /**
     * Set idx points to the nearest next no-blank byte.
     */
    protected void ignoreBlankSpace() {
        while (!isEnd() && isBlankSpace()) {
            nextByte();
        }
    }

    protected void nextByte() {
        if (idx < buffer.length - 1) {
            b = buffer[++idx];
        } else if (idx == buffer.length - 1){
            idx++;
        }
    }

    protected boolean isLetter() {
        return b >= 'a' && b <= 'z' || b >= 'A' && b <= 'Z';
    }

    protected boolean isDigit() {
        return b >= '0' && b <= '9';
    }

    protected boolean isBlankSpace() {
        return b == ' ';
    }

    protected boolean isSingleSymbol() {
        return b == '=' || b == '-' || b == '*' || b == '(' || b == ')' || b == ';';
    }

    protected boolean isMultiSymbol() {
        return b == '<' || b == '>' || b == ':';
    }

    protected boolean isMultiSymbolSuffix() {
        return b == '=' || b == '>';
    }

    protected boolean isReservedWord(String word) {
        return SymbolManager.containsReservedToken(word);
    }

    protected boolean isEOLN() {
        if (b == '\r') {
            nextByte();
            if (b == '\n') {
                retract();
                return true;
            }

            retract();
        }
        return false;
    }

    protected void retract() {
        b = buffer[--idx];
    }

    protected boolean isEnd() {
        return idx >= buffer.length;
    }

    protected void handleConstant() {
        tokenBuilder.append(b);
        nextByte();
        while (!isEnd()) {
            if (isDigit()) {
                tokenBuilder.append(b);
                nextByte();
            } else {
                break;
            }
        }

        token = new LexicalToken(tokenBuilder.toString(), SymbolManager.CONSTANT_TOKEN_TYPE);
    }

    protected void handleWord() {
        tokenBuilder.append(b);
        nextByte();
        while (!isEnd()) {
            if (isLetter() || isDigit()) {
                tokenBuilder.append(b);
                nextByte();
            } else {
                break;
            }
        }
        if ((token = SymbolManager.getReservedToken(tokenBuilder.toString())) == null) {
            token = new LexicalToken(tokenBuilder.toString(), SymbolManager.IDENTIFIER_TOKEN_TYPE);
        }
    }

    protected void handleSingleSymbol() throws LexicalException {
        tokenBuilder.append(b);
        nextByte();
        if ((token = SymbolManager.getReservedToken(tokenBuilder.toString())) == null) {
            throw new LexicalException("***LINE:" + line + "  Unexpected symbol.");
        }
    }

    protected void handleMultiSymbol() throws LexicalException {
        tokenBuilder.append(b);
        nextByte();

        if (isMultiSymbolSuffix()) {
            tokenBuilder.append(b);
            nextByte();
        }

        if ((token = SymbolManager.getReservedToken(tokenBuilder.toString())) == null) {
            throw new LexicalException("***LINE:" + line + "  Unexpected symbol.");
        }
    }

    protected void handleEOLN() {
        token = SymbolManager.getReservedToken("EOLN");
        line++;
        nextByte();
        nextByte();
    }
}
