package globaz.hermes.zas;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.hermes.application.HEApplication;
import globaz.hermes.utils.EBCDICFileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author ald Process permettant de séparer les annonces en envoi par Hermes de celles en envoi par Pavo, ceci utilisé
 *         exclusivement pour les tests batchs 31.01.2005
 */
public class HERemoveExtraitCI {
    public static final String ENCODING = "Cp037";

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                System.out.println("Usage java globaz.hermes.zas.HERemoveExtraitCI <input> <user> <pwd> ");
                System.exit(-1);
            }
            BSession session = new BSession("HERMES");
            session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES").newSession(args[1], args[2]);
            HERemoveExtraitCI process = new HERemoveExtraitCI();
            process.splitFile(args[0], session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(-1);
        }
    }

    private char[] fileBuffer = new char[120];
    public boolean hasCarriageReturns = true;
    public boolean isFileEBCDIC = false;
    private int nbRecords = 0;
    public BufferedReader r = null;

    public File sourceFile = null;

    /**
     * Constructor for HERemoveExtraitCI.
     */
    public HERemoveExtraitCI() {
        super();
    }

    /**
     * @param line
     */
    private String getDate(String line) {
        String jj = line.substring(26, 28);
        // JJMMAA
        String mm = line.substring(28, 30);
        // JJMMAA
        String aa = line.substring(30, 32);
        return jj + mm + aa;
    }

    public String getLotFooter(String date, BSession session) throws Exception {
        String footer = new String("9901");
        footer += session.getApplication().getProperty("noCaisse") + session.getApplication().getProperty("noAgence");
        // footer += "026001";
        footer = globaz.hermes.utils.StringUtils.padAfterString(footer, " ", 24);
        footer += "T0" + date;
        footer += globaz.hermes.utils.StringUtils.padBeforeString(nbRecords + "", "0", 6);
        // footer += globaz.hermes.utils.StringUtils.padBeforeString("13" + "",
        // "0", 6);
        if ("true".equals(session.getApplication().getProperty("ftp.test"))) {
            footer += "TEST";
        } else {
            footer += "    ";
        }
        footer = globaz.hermes.utils.StringUtils.padAfterString(footer, " ", 120);
        return footer;
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

    public void splitFile(String file, BSession session) throws Exception {
        sourceFile = new File(file);
        isFileEBCDIC = "true".equals(((HEApplication) session.getApplication()).getProperty("ftp.file.input.ebcdic"));
        /** config fichier, le fichier reçu a-t'il des carriage return ? * */
        hasCarriageReturns = "true".equals(((HEApplication) session.getApplication())
                .getProperty("ftp.file.input.carriagereturn"));
        if (isFileEBCDIC) {
            // je lis de l'EBCDIC
            r = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), ENCODING));
        } else {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
        }
        File out = new File(file + "_out");
        if (out.exists()) {
            out.delete();
        }
        EBCDICFileWriter fos = new EBCDICFileWriter(out);
        String line = "";
        String date = "";
        while ((line = readLine(r, hasCarriageReturns)) != null) {
            if (line.startsWith("01")) {
                fos.write(line);
                date = getDate(line);
            }
            if (line.startsWith("11")) {
                fos.write(line);
                nbRecords++;
            }
            if (line.startsWith("38")) {
                fos.write(getLotFooter(date, session));
                break;
            }
        }
        fos.close();
        r.close();
    }
}
