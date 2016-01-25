package globaz.pavo.db.splitting;

import globaz.globall.db.BApplication;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Classe contenant la liste des rentes minimales par année. Date de création : (11.11.2002 15:33:12)
 * 
 * @author: David Girardin
 */
public class RenteMinimale {
    private static final String DELIM = ";";
    private static final String FILE_PROPERTY = "rentesURL";
    private static HashMap table;

    /**
     * Retourne les rentes minimales pour l'année donnée. Date de création : (11.11.2002 16:05:13)
     * 
     * @return les rentes minimales pour l'année donnée ou null si l'année spécifiée ne se trouve pas dans la liste.
     * @param annee
     *            l'année en question.
     */
    public static RenteMinimale getRenteMinimale(String annee) {
        if (table == null) {
            loadFile();
        }
        if (table.containsKey(annee)) {
            return (RenteMinimale) table.get(annee);
        }
        return new RenteMinimale(annee, null, null);
    }

    /**
     * Charge le fichier des rentes en mémoire. Date de création : (11.11.2002 16:10:03)
     */
    private static void loadFile() {
        String line;
        StringTokenizer tokenizer;
        RenteMinimale rente;
        // instance de la table des revenus
        table = new HashMap();
        try {
            // ouvre le fichier
            /*
             * String fileURL = GlobazServer .getCurrentSystem() .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO)
             * .getProperty(FILE_PROPERTY); URL file = new URL(fileURL); BufferedReader reader = new BufferedReader(new
             * InputStreamReader(file.openStream()));
             */
            BApplication application = (BApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            String file;
            if (JAUtil.isStringEmpty(application.getLocalPath())) {
                file = "./pavoRoot/rentes.csv";
            } else {
                file = application.getLocalPath() + "/pavoRoot/rentes.csv";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    RenteMinimale.class.getResourceAsStream("/" + "rentes.csv")));
            // création de la liste des rentes
            while ((line = reader.readLine()) != null) {
                tokenizer = new StringTokenizer(line, DELIM, true);
                String anneeRente = tokenizer.nextToken();
                tokenizer.nextToken();
                String renteVeuf = tokenizer.nextToken();
                if (DELIM.equals(renteVeuf)) {
                    renteVeuf = null;
                } else {
                    tokenizer.nextToken();
                }
                String renteAVS;
                if (tokenizer.hasMoreTokens()) {
                    renteAVS = tokenizer.nextToken();
                } else {
                    renteAVS = null;
                }
                rente = new RenteMinimale(anneeRente, renteVeuf, renteAVS);
                table.put(anneeRente, rente);
            }
        } catch (Exception ex) {
            // erreur de chargement (fichier invalide) -> laisse la table vide
            ex.printStackTrace();
            table = null;
        }
    }

    private int annee;

    private int rmAVS;

    private int rmVeuf;

    private boolean stateVeufOK = true;

    /**
     * Constructeur.
     */
    public RenteMinimale(String annee, String rmVeuf, String rmAVS) {
        super();
        try {
            this.annee = Integer.parseInt(annee);
            if (rmVeuf == null) {
                this.rmVeuf = 1;
                // changement 21.07.04 -> si > 1974, rente non définie mais ok.
                // Calcul par année
                if (this.annee >= 1974) {
                    stateVeufOK = false;
                }
                // fin changement
            } else {
                this.rmVeuf = Integer.parseInt(rmVeuf);
            }
            if (rmAVS == null) {
                this.rmAVS = 1;
            } else {
                this.rmAVS = Integer.parseInt(rmAVS);
            }
        } catch (Exception ex) {
            // si une exception survient -> fichier invalide
            this.rmVeuf = 0;
            this.rmAVS = 0;
        }
    }

    /**
     * Teste si la rente minimale de veuf a bien été trouvée. Date de création : (15.11.2002 11:43:50)
     * 
     * @return false si le fichier n'a pas pu être lu ou si la rente n'est pas spécifiée dans le fichier pour l'année
     *         concernée.
     */
    public boolean estRenteVeufDéfinie() {
        return stateVeufOK;
    }

    /**
     * L'année concernée. Date de création : (11.11.2002 16:31:18)
     * 
     * @return l'année concernée
     */
    private int getAnnee() {
        return annee;
    }

    /**
     * La rente minimale AVS. Date de création : (11.11.2002 16:31:18)
     * 
     * @return la rente minimale AVS.
     */
    public int getRmAVS() {
        return rmAVS;
    }

    /**
     * La rente minimale de veuf Date de création : (11.11.2002 16:31:18)
     * 
     * @return la rente minimale de veuf.
     */
    public int getRmVeuf() {
        return rmVeuf;
    }
}
