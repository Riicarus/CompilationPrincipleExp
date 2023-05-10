import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-10 10:33
 * @since 1.0.0
 */
public class SymbolManager {

    private static final HashMap<String, LexicalToken> RESERVED_SYMBOL_TOKENS = new HashMap<>();

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

    public static boolean containsReservedToken(String name) {
        return RESERVED_SYMBOL_TOKENS.containsKey(name);
    }

    private static final HashSet<GrammarVariable> VARIABLES = new HashSet<>();

    private static final HashSet<GrammarProcess> PROCESSES = new HashSet<>();

    public static void addVariable(GrammarVariable variable) {
        VARIABLES.add(variable);
    }

    public static boolean containsVariable(GrammarVariable variable) {
        return VARIABLES.contains(variable);
    }

    public static boolean containsVariable(String name, int level, Stack<GrammarProcess> processStack) {
        for (GrammarVariable variable : VARIABLES) {
            for (GrammarProcess process : processStack) {
                if (variable.getVname().equals(name) && variable.getVproc().equals(process.getPname()) && variable.getVlev() <= level) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getVariableIdx() {
        return VARIABLES.size();
    }

    public static void clearVariable() {
        VARIABLES.clear();
    }

    public static void addProcess(GrammarProcess process) {
        PROCESSES.add(process);
    }

    public static boolean containsProcess(GrammarProcess process) {
        return PROCESSES.contains(process);
    }

    public static void clearProcess() {
        PROCESSES.clear();
    }

    public static HashSet<GrammarVariable> getVARIABLES() {
        return VARIABLES;
    }

    public static HashSet<GrammarProcess> getPROCESSES() {
        return PROCESSES;
    }
}
