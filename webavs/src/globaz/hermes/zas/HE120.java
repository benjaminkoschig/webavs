package globaz.hermes.zas;

import globaz.hermes.utils.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author ado 10 nov. 03
 */
public class HE120 {
    public static void main(String[] args) {
        try {
            new HE120().go(args[0]);
            new HE120().go2(args[0]);
            // new HE120().go3(args[0]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private char[] fileBuffer = new char[120];

    /**
     * Constructor for HE120.
     */
    public HE120() {
        super();
    }

    /**
     * Method go.
     * 
     * @param string
     */
    private void go(String fileIn) throws Exception {
        BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileIn + "120")));
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn), HEReprise.ENCODING));
        String line = "";
        while ((line = readLine(r, false)) != null) {
            fos.write(line + "\n");
        }
        fos.close();
        r.close();
    }

    private void go2(String fileIn) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));
        BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileIn + "EBDIC"), "Cp037"));
        String line;
        while ((line = r.readLine()) != null) {
            fos.write(StringUtils.padAfterString(line, " ", 120));
        }
        fos.close();
        r.close();
    }

    private void go3(String fileIn) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));
        BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileIn + "120Online")));
        String line;
        while ((line = r.readLine()) != null) {
            fos.write(line);
        }
        fos.close();
        r.close();
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
