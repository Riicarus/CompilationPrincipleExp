import java.util.Objects;

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

    public GrammarProcess(String pname, String ptype, int plev, int fadr, int ladr) {
        this.pname = pname;
        this.ptype = ptype;
        this.plev = plev;
        this.fadr = fadr;
        this.ladr = ladr;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public int getPlev() {
        return plev;
    }

    public void setPlev(int plev) {
        this.plev = plev;
    }

    public int getFadr() {
        return fadr;
    }

    public void setFadr(int fadr) {
        this.fadr = fadr;
    }

    public int getLadr() {
        return ladr;
    }

    public void setLadr(int ladr) {
        this.ladr = ladr;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarProcess that = (GrammarProcess) o;
        return plev == that.plev && Objects.equals(pname, that.pname) && Objects.equals(ptype, that.ptype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pname, ptype, plev);
    }
}
