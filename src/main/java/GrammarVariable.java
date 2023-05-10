import java.util.Objects;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-9 14:56
 * @since 1.0.0
 */
public class GrammarVariable {

    private String vname;

    private String vproc;

    private int vkind;

    private String vtype;

    private int vlev;

    private int vadr;

    public GrammarVariable(String vname, String vproc, int vkind, String vtype, int vlev, int vadr) {
        this.vname = vname;
        this.vproc = vproc;
        this.vkind = vkind;
        this.vtype = vtype;
        this.vlev = vlev;
        this.vadr = vadr;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVproc() {
        return vproc;
    }

    public void setVproc(String vproc) {
        this.vproc = vproc;
    }

    public int getVkind() {
        return vkind;
    }

    public void setVkind(int vkind) {
        this.vkind = vkind;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public int getVlev() {
        return vlev;
    }

    public void setVlev(int vlev) {
        this.vlev = vlev;
    }

    public int getVadr() {
        return vadr;
    }

    public void setVadr(int vadr) {
        this.vadr = vadr;
    }

    @Override
    public String toString() {
        return "{" +
                "vname='" + vname + '\'' +
                ", vproc='" + vproc + '\'' +
                ", vkind=" + vkind +
                ", vtype='" + vtype + '\'' +
                ", vlev=" + vlev +
                ", vadr=" + vadr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarVariable variable = (GrammarVariable) o;
        return vlev == variable.vlev && Objects.equals(vname, variable.vname) && Objects.equals(vproc, variable.vproc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vname, vproc, vlev);
    }
}
