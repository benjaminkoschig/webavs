package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class CIDeclarationMilitaireIterator implements ICIDeclarationIterator {

    private BufferedReader br = null;
    private FileInputStream fi = null;
    private String filename = "";
    private boolean isReady = false;
    // structaure d'un élement a lire (une ligne)
    private String line = null; // doir être initaialisé a null !
    private String provenance = "";
    BSession session = null;
    private int size = -1;
    private BTransaction transaction = null;

    /**
     * Rempli un objet CIDeclaration à partir d'une ligne du fichier plat
     * 
     */
    private CIDeclarationRecord _fillBean(String line) {
        // champs
        String agence = line.substring(7, 10);
        String noAvs = line.substring(16, 27);
        String numeroAffilie = line.substring(29, 39);
        String greEc = line.substring(39, 41);
        int moisDebut = Integer.parseInt(line.substring(41, 43));
        int moisFin = Integer.parseInt(line.substring(43, 45));
        String annee = line.substring(47, 49);
        String montantEcr = line.substring(49, 58);
        // String anneeInsc = line.substring(58,60);

        //
        if (Integer.valueOf(annee).intValue() > 60) {
            annee = 19 + annee;
        } else {
            annee = 20 + annee;
        }
        CIDeclarationRecord dec = new CIDeclarationRecord();
        dec.setNumeroAvs(noAvs);
        dec.setNumeroAffilie(numeroAffilie);
        dec.setGenreEcriture(greEc);
        dec.setAnnee(annee);
        dec.setMoisDebut(moisDebut);
        dec.setMoisFin(moisFin);
        dec.setMontantEcr(montantEcr);
        dec.setAgence(agence);

        dec.setMontantPositif(true);
        return dec;
    }

    private void _init() throws Exception {
        try {
            if (!isReady) {
                fi = new FileInputStream(getFilename());
                br = new BufferedReader(new InputStreamReader(fi));
                isReady = true;
            }
        } catch (Exception e) {
            // on catch l'exception pour pouvoir fermer correctement les fichier
            // au besoin
            close();
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

    @Override
    public String getDateReception() {
        return null;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public TreeMap getNbSalaires() {
        return null;
    }

    @Override
    public TreeMap getNoAffiliePourReception() throws Exception {
        return null;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    @Override
    public TreeMap getTotauxJournaux() {
        return null;
    }

    /**
     * Returns the transaction.
     * 
     * @return BTransaction
     */
    @Override
    public BTransaction getTransaction() {
        return transaction;
    }

    @Override
    public boolean hasNext() throws Exception {
        if (!isReady) {
            _init();
        }
        if ((line != null) && !" ".equals(line.substring(0, 1))) {
            // la ligne a déjà été lue, mas pas encore été retourné par next();
            return true;
        } else {
            // lecture de la prochaine ligne, (mis en cache pour que le prochain
            // next() n'aie
            // pas besoin de relire cette ligne)
            line = br.readLine();
            while (" ".equals(line.substring(0, 1))) {
                line = br.readLine();
            }
            if (line != null) {
                while (!"32".equals(line.substring(0, 2))) {

                    line = br.readLine();
                    if (line == null) {
                        close();
                        return false;
                    }

                    if (!"34".equals(line.substring(0, 2)) && !"32".equals(line.substring(0, 2))
                            && !"31".equals(line.substring(0, 2))) {
                        close();
                        return false;
                    }

                }
                // le fichier n'est pas terminé, on a trouvé une ligne
                if (line != null) {
                    return true;
                }
            }
        }
        // fin du fichier, on a pas trouvé de ligne a lire.

        close();

        return false;
    }

    public void init() {
    }

    @Override
    public CIDeclarationRecord next() throws Exception {
        String tmpLine = null;
        if (!isReady) {
            _init();
        }
        try {
            // si il n'y a rien dans le cache, on lit dans le fichier
            if ((line == null) || " ".equals(line.substring(0, 1))) {
                line = br.readLine();
                // saute les lignes vide

                while ("".equals(line) || " ".equals(line.substring(0, 1)) || !("32".equals(line.substring(0, 2)))) {

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
        CIDeclarationRecord declaration = _fillBean(line);
        // vide le cache pour lire de toute facon la ligne suivante
        // System.out.println("["+line+"]");
        if ((line.length() <= 60) || " ".equals(line.substring(0, 1))) {
            line = null;
        } else {
            line = line.substring(60, 120);
        }
        return declaration;
    }

    /**
     * Sets the filename.
     * 
     * @param filename
     *            The filename to set
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    @Override
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Sets the transaction.
     * 
     * @param transaction
     *            The transaction to set
     */
    @Override
    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void setTypeImport(String type) {
    }

    /**
     * retourne le nombre de record contenu dans le fichier, -1 en cas de probleme
     */
    @Override
    public int size() throws Exception {
        try {

            if (size == -1) {
                ICIDeclarationIterator it = new CIDeclarationMilitaireIterator();
                it.setFilename(getFilename());
                size = 0;
                while (it.hasNext()) {
                    CIDeclarationRecord dec = it.next();
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