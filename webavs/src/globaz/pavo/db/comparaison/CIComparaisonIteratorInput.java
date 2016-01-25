package globaz.pavo.db.comparaison;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CIComparaisonIteratorInput implements ICIComparaisonIteratorInput {

    private BufferedReader br = null;
    private FileInputStream fi = null;
    private String fileName = "";
    private boolean isReady = false;
    private String line = null;

    private int size = -1;

    public CIComparaisonIteratorInput() {
        super();
    }

    private CIEnteteRecord _fillBean(String line) {
        CIEnteteRecord record = new CIEnteteRecord();
        record.setCaisse(line.substring(4, 7));
        record.setAgence(line.substring(7, 10));
        record.setNumeroAssure(line.substring(10, 21));
        if (record.getNumeroAssure().startsWith("-") && record.getNumeroAssure().length() > 1) {
            record.setNumeroAssure("756" + record.getNumeroAssure().substring(1));
        }
        record.setNumeroAssureAnterieur(line.substring(21, 32));
        record.setNomPrenom(line.substring(32, 72));
        record.setPays(line.substring(72, 75));
        record.setMotifOuverture(line.substring(75, 77));
        record.setAnneeOuverture(line.substring(77, 79));
        record.setMotifRci(line.substring(79, 81));
        record.setDateCloture(line.substring(81, 85));
        record.setCaisseCommettante(line.substring(85, 88));
        record.setAgenceCommettante(line.substring(88, 91));
        return record;
    }

    private void _init() throws Exception {
        try {
            if (!isReady) {
                fi = new FileInputStream(getFileName());
                br = new BufferedReader(new InputStreamReader(fi));
                isReady = true;
            }
        } catch (Exception e) {
            // on catch l'exception pour pouvoir fermer correctement les fichier
            // au besoin
            // close();
            // puis on transmet l'exception plus haut
            throw e;
        }
    }

    @Override
    public void close() {
        if (fi != null) {
            try {
                fi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isReady = false;
    }

    /**
     * @return
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean hasNext() throws Exception {
        if (!isReady) {
            _init();
        }
        // lecture de la prochaine ligne, (mis en cache pour que le prochain
        // next() n'aie
        // pas besoin de relire cette ligne)
        line = br.readLine();

        if (line != null) {
            while (!"24".equals(line.substring(0, 2))) {
                line = br.readLine();
                if (line == null) {
                    close();
                    return false;
                }

            }
            // le fichier n'est pas terminé, on a trouvé une ligne
            if (line != null) {
                return true;
            }
        }

        // fin du fichier, on a pas trouvé de ligne a lire.

        close();

        return false;
    }

    @Override
    public CIEnteteRecord next() throws Exception {
        String tmpLine = null;
        if (!isReady) {
            _init();
        }
        try {
            // si il n'y a rien dans le cache, on lit dans le fichier
            if (line == null || " ".equals(line.substring(0, 1))) {
                line = br.readLine();
                // saute les lignes vide

                while ("".equals(line) || " ".equals(line.substring(0, 1)) || !("24".equals(line.substring(0, 2)))) {

                    line = br.readLine();
                }
            }
        } catch (Exception e) {
            // catch l'Exception pour pouvoir fermer le fichier si besoin
            close();
            // puis remonte l'Exception
            throw e;
        }
        // séparation des champs de la ligne
        CIEnteteRecord declaration = _fillBean(line);
        // CIEnteteRecord declaration = new CIEnteteRecord ();
        // vide le cache pour lire de toute facon la ligne suivante
        // System.out.println("["+line+"]");
        line = null;
        return declaration;
    }

    /**
     * @param string
     */
    @Override
    public void setFileName(String string) {
        fileName = string;
    }

    @Override
    public int size() throws Exception {
        try {

            if (size == -1) {
                CIComparaisonIteratorInput it = new CIComparaisonIteratorInput();
                it.setFileName(getFileName());
                size = 0;
                while (it.hasNext()) {
                    // CIDeclarationRecord dec = it.next();
                    size++;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            size = -1;
        }

        return size;
    }

}
