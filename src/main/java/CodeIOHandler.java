import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * [FEATURE INFO]<br/>
 *
 * @author Riicarus
 * @create 2023-5-8 14:52
 * @since 1.0.0
 */
public class CodeIOHandler {

    private FileReader pasReader;

    private FileWriter dydWriter;
    private FileWriter errWriter;
    private FileWriter dysWriter;
    private FileWriter varWriter;
    private FileWriter proWriter;

    private String srcPath;

    private String dstPath;

    private String filename;

    public void setPath(String srcPath, String dstPath) throws IOException {
        pasReader = new FileReader(srcPath);
        String fullFilename = srcPath.substring(srcPath.lastIndexOf("\\") + 1);
        filename = fullFilename.substring(0, fullFilename.lastIndexOf("."));
        dydWriter = new FileWriter(dstPath + "\\" + filename + ".dyd", false);
        errWriter = new FileWriter(dstPath + "\\" + filename + ".err", false);
        dysWriter = new FileWriter(dstPath + "\\" + filename + ".dys", false);
        varWriter = new FileWriter(dstPath + "\\" + filename + ".var", false);
        proWriter = new FileWriter(dstPath + "\\" + filename + ".pro", false);

        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }

    @SuppressWarnings("all")
    public char[] readCodeFromFile() throws IOException {
        File file = new File(srcPath);
        char[] buf = new char[(int) file.length()];
        pasReader.read(buf);

        return buf;
    }

    public boolean checkErrFile() {
        File file = new File(dstPath + "\\" + filename + ".err");
        return file.length() == 0;
    }

    public void appendDyd(CharSequence cs) throws IOException {
        dydWriter.append(cs);
    }

    public void appendErr(CharSequence cs) throws IOException {
        errWriter.append(cs);
    }

    public void flushForLexical() throws IOException {
        errWriter.flush();
        dydWriter.flush();
    }

    public void flushForGrammar() throws IOException {
        dysWriter.flush();
        varWriter.flush();
        proWriter.flush();
    }

    public void close() throws IOException {
        pasReader.close();
        errWriter.close();
        dydWriter.close();
        dysWriter.close();
        varWriter.close();
        proWriter.close();
    }
}
