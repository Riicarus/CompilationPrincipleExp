/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-9 14:58
 * @since 1.0.0
 */
public class GrammarProcess {

    private String pname;

    private String ptype;

    private int plev;

    private int fadr;

    private int ladr;

    @Override
    public String toString() {
        return "{" +
                "pname='" + pname + '\'' +
                ", ptype='" + ptype + '\'' +
                ", plev=" + plev +
                ", fadr=" + fadr +
                ", ladr=" + ladr +
                '}';
    }
}
