package globaz.hermes.zas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author ado 10 nov. 03
 */
public class HEACTreat {
    public static void main(String[] args) {
        try {
            new HEACTreat().go(args[0]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private char[] fileBuffer = new char[120];

    /**
     * Constructor for HE120.
     */
    public HEACTreat() {
        super();
    }

    /**
     * Method go.
     * 
     * @param string
     */
    private void go(String fileIn) throws Exception {
        // Directory path here
        // String path = ".";

        String files;
        File folder = new File(fileIn);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                BufferedWriter fos = null;
                BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn + "/" + files),
                        HEReprise.ENCODING));
                String line = "";
                int j = 0;
                while ((line = readLine(r, false)) != null) {
                    if (line.startsWith("3101") || line.startsWith("3201") || line.startsWith("3401")) {
                        j++;
                        if (j == 1) {
                            fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileIn + "/" + files
                                    + "AC_120")));
                        }
                        fos.write(line + "\n");
                    }
                }
                if (fos != null) {
                    fos.close();
                }
                r.close();
            }
        }
    }

    private String readLine(BufferedReader file, boolean hasCarridgeReturn) throws Exception {

        if (hasCarridgeReturn) {
            return file.readLine();
        } else {
            int nread = 0;

            if ((nread = file.read(fileBuffer)) >= 0) {
                return String.valueOf(fileBuffer, 0, nread);
            } else {
                return null;
            }
        }
    }

}
