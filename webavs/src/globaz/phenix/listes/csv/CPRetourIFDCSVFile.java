package globaz.phenix.listes.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CPRetourIFDCSVFile {

    private String filename = "";
    private ArrayList<?> liste = null;

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        try {
            File f = File.createTempFile(getFilename(), ".csv");
            f.deleteOnExit();
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
        liste = (ArrayList<?>) obj;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void write(FileOutputStream out) {

        PrintStream ps = new PrintStream(out);
        try {

            for (Iterator<?> it = liste.iterator(); it.hasNext();) {
                ps.println((String) it.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

}
