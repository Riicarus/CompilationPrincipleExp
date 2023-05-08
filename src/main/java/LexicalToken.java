import java.util.Objects;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-7 16:50
 * @since 1.0.0
 */
public class LexicalToken {

    private String symbol;

    private int type;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LexicalToken(String symbol, int type) {
        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public String toString() {
        return symbol + ' ' + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LexicalToken that = (LexicalToken) o;
        return type == that.type && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, type);
    }
}
