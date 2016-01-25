package globaz.pavo.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jwe
 * @since 12 février 2014
 * 
 */
public class CITransfertAffiliesProcessCSV {

    private static final String SEPARATION = ";";
    private List<String> arrayCsv = null;
    private String filename = "";
    private List<String> liste = null;

    public CITransfertAffiliesProcessCSV() {
        arrayCsv = new ArrayList<String>();
        String title = "Affilié source" + ";" + "Affilié destination" + ";" + "Assuré(e)" + ";" + "Numéro NSS" + ";"
                + "Date d'engagement" + ";" + "Résultat transfert" + ";" + "Message d'erreur" + ";";
        arrayCsv.add(title);

    }

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        try {
            File f = new File(getFilename() + ".csv");
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

    // ajouter le nss et la date d'engagement
    public CITransfertAffiliesProcessCSV populateSheet(String nomAssure, String nss, String dateEngagement,
            String affilieSource, String affilieDistant, String result, String cause) throws Exception {

        StringBuilder line = new StringBuilder();

        // données
        line.append(affilieSource).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(affilieDistant).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(nomAssure).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(nss).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(dateEngagement).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(result).append(CITransfertAffiliesProcessCSV.SEPARATION);
        line.append(cause).append(CITransfertAffiliesProcessCSV.SEPARATION);

        arrayCsv.add(line.toString());

        setData(arrayCsv);
        return this;
    }

    public void setData(List<String> listeInfo) {
        liste = listeInfo;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void write(FileOutputStream out) {

        PrintStream ps = new PrintStream(out);
        try {
            for (String info : liste) {
                ps.println(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

}
