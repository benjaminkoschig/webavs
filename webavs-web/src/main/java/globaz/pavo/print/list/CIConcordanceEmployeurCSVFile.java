package globaz.pavo.print.list;

import globaz.jade.common.Jade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CIConcordanceEmployeurCSVFile {

    private String filename = "";
    private ArrayList liste = null;

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        try {
            File f = new File(Jade.getInstance().getSharedDir() + getFilename());
            System.out.print("\n fichier de logg => " + f.getAbsolutePath());

            // f.deleteOnExit();

            FileOutputStream out = new FileOutputStream(f);
            write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    public void setData(Object obj) {
        liste = (ArrayList) obj;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void write(FileOutputStream out) {

        PrintStream ps = new PrintStream(out);
        try {

            for (Iterator it = liste.iterator(); it.hasNext();) {
                ps.println((String) it.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

}
