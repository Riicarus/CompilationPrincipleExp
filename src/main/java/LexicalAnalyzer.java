import java.util.HashMap;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-7 16:44
 * @since 1.0.0
 */
public class LexicalAnalyzer {

    public static final HashMap<String, LexicalToken> RESERVED_SYMBOL_TOKENS = new HashMap<>();

    public static final int IDENTIFIER_TOKEN_TYPE = 10;
    public static final int CONSTANT_TOKEN_TYPE = 11;

    static {
        RESERVED_SYMBOL_TOKENS.put("begin", new LexicalToken("begin", 1));
        RESERVED_SYMBOL_TOKENS.put("end", new LexicalToken("end", 2));
        RESERVED_SYMBOL_TOKENS.put("integer", new LexicalToken("integer", 3));
        RESERVED_SYMBOL_TOKENS.put("if", new LexicalToken("if", 4));
        RESERVED_SYMBOL_TOKENS.put("then", new LexicalToken("then", 5));
        RESERVED_SYMBOL_TOKENS.put("else", new LexicalToken("else", 6));
        RESERVED_SYMBOL_TOKENS.put("function", new LexicalToken("function", 7));
        RESERVED_SYMBOL_TOKENS.put("read", new LexicalToken("read", 8));
        RESERVED_SYMBOL_TOKENS.put("write", new LexicalToken("write", 9));
        RESERVED_SYMBOL_TOKENS.put("=", new LexicalToken("=", 12));
        RESERVED_SYMBOL_TOKENS.put("<>", new LexicalToken("<>", 13));
        RESERVED_SYMBOL_TOKENS.put("<=", new LexicalToken("<=", 14));
        RESERVED_SYMBOL_TOKENS.put("<", new LexicalToken("<", 15));
        RESERVED_SYMBOL_TOKENS.put(">=", new LexicalToken(">=", 16));
        RESERVED_SYMBOL_TOKENS.put(">", new LexicalToken(">", 17));
        RESERVED_SYMBOL_TOKENS.put("-", new LexicalToken("-", 18));
        RESERVED_SYMBOL_TOKENS.put("*", new LexicalToken("*", 19));
        RESERVED_SYMBOL_TOKENS.put(":=", new LexicalToken(":=", 20));
        RESERVED_SYMBOL_TOKENS.put("(", new LexicalToken("(", 21));
        RESERVED_SYMBOL_TOKENS.put(")", new LexicalToken(")", 22));
        RESERVED_SYMBOL_TOKENS.put(";", new LexicalToken(";", 23));
        RESERVED_SYMBOL_TOKENS.put("EOLN", new LexicalToken("EOLN", 24));
        RESERVED_SYMBOL_TOKENS.put("EOF", new LexicalToken("EOF", 25));
    }

    private final StringBuilder tokenBuilder = new StringBuilder();

    private LexicalToken token;

    private char[] buffer;

    private int idx = 0;

    private int line = 1;

    private char b;

    private boolean ended;

    public LexicalAnalyzer() {
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

    public LexicalToken analyzeOne() throws LexicalException {
        resetForNextToken();

        ignoreBlankSpace();

        if (isEnd()) {
            if (!ended) {
                token = RESERVED_SYMBOL_TOKENS.get("EOF");
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
        return RESERVED_SYMBOL_TOKENS.containsKey(word);
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

        token = new LexicalToken(tokenBuilder.toString(), CONSTANT_TOKEN_TYPE);
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
        if ((token = RESERVED_SYMBOL_TOKENS.get(tokenBuilder.toString())) == null) {
            token = new LexicalToken(tokenBuilder.toString(), IDENTIFIER_TOKEN_TYPE);
        }
    }

    protected void handleSingleSymbol() throws LexicalException {
        tokenBuilder.append(b);
        nextByte();
        if ((token = RESERVED_SYMBOL_TOKENS.get(tokenBuilder.toString())) == null) {
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

        if ((token = RESERVED_SYMBOL_TOKENS.get(tokenBuilder.toString())) == null) {
            throw new LexicalException("***LINE:" + line + "  Unexpected symbol.");
        }
    }

    protected void handleEOLN() {
        token = RESERVED_SYMBOL_TOKENS.get("EOLN");
        line++;
        nextByte();
        nextByte();
    }
}
