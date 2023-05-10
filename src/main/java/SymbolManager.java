import java.util.HashMap;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-10 10:33
 * @since 1.0.0
 */
public class SymbolManager {

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

    public static LexicalToken getReservedToken(String name) {
        return RESERVED_SYMBOL_TOKENS.get(name);
    }
}
