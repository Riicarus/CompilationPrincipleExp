import com.riicarus.comandante.main.CommandLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * [FEATURE INFO]<br/>
 * Analyze grammar
 *
 * @author Riicarus
 * @create 2023-5-8 21:37
 * @since 1.0.0
 */
public class GrammarAnalyzer {

    private final CodeIOHandler ioHandler;

    private final List<LexicalToken> tokens = new LinkedList<>();

    private final List<GrammarVariable> variables = new LinkedList<>();

    private final List<GrammarProcess> processes = new LinkedList<>();

    private int tokenIdx = 0;

    private final Stack<Integer> retractIdxStack = new Stack<>();

    private int line = 1;

    private LexicalToken currentToken;

    private GrammarException prevException;

    public GrammarAnalyzer(CodeIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void input(List<LexicalToken> tokens) {
        this.tokens.clear();
        this.tokens.addAll(tokens);

        reset();
    }

    private void reset() {
        variables.clear();
        processes.clear();
        tokenIdx = 0;
        retractIdxStack.clear();
        line = 1;
        currentToken = null;
    }

    public void doAnalyze() throws GrammarException {
        if (tokens.size() == 0) {
            return;
        }

        currentToken = tokens.get(0);

        try {
            S();
        } catch (GrammarException e) {
            if (!isEnd()) {
                throw e;
            }
        }

        if (!isEnd()) {
            throw prevException;
        }

        CommandLogger.log("Grammar analyze succeeded.");
    }

    private void next() {
        tokenIdx++;

        if (tokenIdx == tokens.size()) {
            return;
        }
        currentToken = tokens.get(tokenIdx);

        System.out.println("next: " + currentToken);

        if (currentToken.equals(LexicalAnalyzer.getReserved("EOLN"))) {
            next();
            line++;
        }
    }

    private void setRetractPoint() {
        retractIdxStack.push(tokenIdx);
    }

    private void unsetRetractPoint() {
        retractIdxStack.pop();
    }

    private void retract() {
        tokenIdx = retractIdxStack.pop();
        currentToken = tokens.get(tokenIdx);

        System.out.println("retract: " + currentToken);
    }

    @SuppressWarnings("all")
    private boolean isEnd() {
        return tokenIdx >= tokens.size();
    }

    private void checkReservedWord(String name) throws GrammarException {
        if (!currentToken.equals(LexicalAnalyzer.getReserved(name))) {
            throw new GrammarException("***LINE:" + line + "  expected: \"" + name + "\", but get: \"" + currentToken.getSymbol() + "\"");
        }
        next();
    }

    private void checkConstant() throws GrammarException {
        if (currentToken.getType() != LexicalAnalyzer.CONSTANT_TOKEN_TYPE) {
            throw new GrammarException("***LINE:" + line + "  expected: a constant, but get: \"" + currentToken.getSymbol() + "\"");
        }
        next();
    }

    private void checkIdentifier() throws GrammarException {
        if (currentToken.getType() != LexicalAnalyzer.IDENTIFIER_TOKEN_TYPE) {
            throw new GrammarException("***LINE:" + line + "  expected: a identifier, but get: \"" + currentToken.getSymbol() + "\"");
        }
        next();
    }

    /**
     * Program
     */
    private void S() throws GrammarException {
        SP();
        checkReservedWord("EOF");
    }

    /**
     * Child Program
     */
    private void SP() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("begin");
            Dt();
            checkReservedWord(";");
            Et();
            checkReservedWord("end");

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Description table
     */
    private void Dt() throws GrammarException {
        setRetractPoint();

        try {
            D();
            DtP();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    private void DtP() throws GrammarException {
        setRetractPoint();
        try {
            if (!isEnd()) {
                checkReservedWord(";");
                D();
                DtP();
            }

            unsetRetractPoint();
        } catch (GrammarException e) {
            prevException = e;
            retract();
        }
    }

    /**
     * Execution table
     */
    private void Et() throws GrammarException {
        setRetractPoint();

        try {
            E();
            EtP();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    private void EtP() {
        setRetractPoint();

        try {
            if (!isEnd()) {
                checkReservedWord(";");
                E();
                EtP();
            }

            unsetRetractPoint();
        } catch (GrammarException e) {
            prevException = e;
            retract();
        }
    }

    /**
     * Description Statement
     */
    private void D() throws GrammarException {
        try {
            setRetractPoint();

            Vd();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            prevException = e;
            retract();
        }

        try {
            setRetractPoint();

            Fd();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Variable description <br/>
     * Need retract
     */
    private void Vd() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("integer");
            V();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Function description <br/>
     * Need Retract
     */
    private void Fd() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("integer");
            checkReservedWord("function");
            I();
            checkReservedWord("(");
            A();
            checkReservedWord(")");
            checkReservedWord(";");
            Fb();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Variable
     */
    private void V() throws GrammarException {
        I();
    }

    /**
     * Identifier
     */
    private void I() throws GrammarException {
        checkIdentifier();
    }

    private void C() throws GrammarException {
        checkConstant();
    }

    /**
     * Function body
     */
    private void Fb() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("begin");
            Dt();
            checkReservedWord(";");
            Et();
            checkReservedWord("end");

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Argument
     */
    private void A() throws GrammarException {
        V();
    }

    /**
     * Execution
     */
    private void E() throws GrammarException {
        try {
            setRetractPoint();
            Er();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();
            Ew();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();
            Ea();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();
            Ec();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Execution of Read
     */
    private void Er() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("read");
            checkReservedWord("(");
            V();
            checkReservedWord(")");

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Execution of Write
     */
    private void Ew() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("write");
            checkReservedWord("(");
            V();
            checkReservedWord(")");

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Execution of Assignment
     */
    private void Ea() throws GrammarException {
        setRetractPoint();

        try {
            V();
            checkReservedWord(":=");
            Ae();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Execution of Condition
     */
    private void Ec() throws GrammarException {
        setRetractPoint();

        try {
            checkReservedWord("if");
            Ce();
            checkReservedWord("then");
            E();
            checkReservedWord("else");
            E();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Algorithm expression
     */
    private void Ae() throws GrammarException {
        setRetractPoint();

        try {
            It();
            AeP();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    private void AeP() {
        setRetractPoint();

        try {
            if (!isEnd()) {
                checkReservedWord("-");
                It();
                AeP();
            }

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }
    }

    /**
     * Item
     */
    private void It() throws GrammarException {
        setRetractPoint();

        try {
            Fa();
            ItP();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    private void ItP() {
        setRetractPoint();

        try {
            if (!isEnd()) {
                checkReservedWord("*");
                Fa();
                ItP();
            }

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }
    }

    /**
     * Factor
     */
    private void Fa() throws GrammarException {
        try {
            setRetractPoint();
            Fe();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();
            V();

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();
            C();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Function execution
     */
    private void Fe() throws GrammarException {
        try {
            setRetractPoint();

            I();
            checkReservedWord("(");
            Ae();
            checkReservedWord(")");

            unsetRetractPoint();
            return;
        } catch (GrammarException e) {
            retract();
            prevException = e;
        }

        try {
            setRetractPoint();

            I();
            checkReservedWord("(");
            V();
            checkReservedWord(")");

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Condition expression
     */
    private void Ce() throws GrammarException {
        setRetractPoint();

        try {
            Ae();
            Sr();
            Ae();

            unsetRetractPoint();
        } catch (GrammarException e) {
            retract();
            throw e;
        }
    }

    /**
     * Symbol of relation <br/>
     * No need to retract.
     */
    private void Sr() throws GrammarException {
        try {
            checkReservedWord("<");
            return;
        } catch (GrammarException e) {
            prevException = e;
        }

        try {
            checkReservedWord("<=");
            return;
        } catch (GrammarException e) {
            prevException = e;
        }

        try {
            checkReservedWord(">");
            return;
        } catch (GrammarException e) {
            prevException = e;
        }

        try {
            checkReservedWord(">=");
            return;
        } catch (GrammarException e) {
            prevException = e;
        }

        try {
            checkReservedWord("=");
            return;
        } catch (GrammarException e) {
            prevException = e;
        }

        checkReservedWord("<>");
    }
}
