package globaz.pavo.db.inscriptions.declaration;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * @author oca
 * 
 *         Chargement des données de déclaration de salaire depuis un fichier plat
 */
public class CIDeclarationTextIterator implements ICIDeclarationIterator {

    private BufferedReader br = null;
    private FileInputStream fi = null;
    private String filename = "";
    private boolean isReady = false;
    // structaure d'un élement a lire (une ligne)
    private String line = null; // doir être initaialisé a null !
    private String provenance = "";
    BSession session = null;

    private int size = -1;

    private TreeMap tableNom = new TreeMap();

    private BTransaction transaction = null;

    /**
     * Rempli un objet CIDeclaration à partir d'une ligne du fichier plat
     * 
     */
    private CIDeclarationRecord _fillBean(String line) {

        // champs
        String numeroAffilie = line.substring(0, 6);
        String numeroAffilieFormate = numeroAffilie.substring(0, 3) + "." + numeroAffilie.substring(3);

        String noAvs = line.substring(6, 17);
        if (!JadeStringUtil.isBlankOrZero(noAvs)) {
            if (noAvs.startsWith("-")) {
                if (NSUtil.unFormatAVS(noAvs).length() > 1) {
                    noAvs = "756" + noAvs.substring(1);
                }
            }
        }
        String nomPrenom = line.substring(17, 57);
        String annee = line.substring(57, 61);
        int moisDebut = Integer.parseInt(line.substring(61, 63));
        int moisFin = Integer.parseInt(line.substring(63, 65));
        String montantEcr = line.substring(65, 74) + "." + line.substring(74, 76);
        boolean montantPositif = "1".equals(line.substring(76, 77));
        // String reserve = line.substring(78);
        // rempli le bean
        CIDeclarationRecord dec = new CIDeclarationRecord();

        // recherche des information supplémentaire qui ne sont pas disponible
        // dans ce support.
        // nom de l'affilie
        if (tableNom.containsKey("numeroAffilieFormate")) {
            dec.setNomAffilie((String) tableNom.get(numeroAffilieFormate));
        } else {

            if (transaction != null) {

                // designation de l'affilié
                TITiersViewBean affilieTiers = null;
                try {
                    TIPersonneAvsManager tiersMgr = new TIPersonneAvsManager();
                    tiersMgr.setSession(transaction.getSession());

                    tiersMgr.setForNumAffilieActuel(numeroAffilieFormate);
                    tiersMgr.find();

                    if (tiersMgr.size() == 0) {
                        dec.setNomAffilie(getTransaction().getSession().getLabel("DT_AUCUN_NOM_AFFILIE"));
                    } else if (tiersMgr.size() > 1) {
                        dec.setNomAffilie(getTransaction().getSession().getLabel("DT_PLUSIEURS_NOMS_AFFILIE"));
                    } else {

                        affilieTiers = (TITiersViewBean) tiersMgr.getFirstEntity();
                        dec.setNomAffilie(affilieTiers.getNom());

                    }
                } catch (Exception e) {
                    dec.setNomAffilie(getTransaction().getSession().getLabel("DT_PROB_NOMS_AFFILIE"));
                }

                // date de debut et de fin de l'affilié
                try {
                    int nbAffiliation = 0;
                    AFAffiliation affiliation = _getAffiliation(transaction, numeroAffilieFormate, annee, nbAffiliation);

                    if (affiliation != null) {
                        dec.setDebutAffiliation(affiliation.getDateDebut());
                        dec.setFinAffiliation(affiliation.getDateFin());

                    } else {
                        if (nbAffiliation == 0) {
                            dec.setNomAffilie(affilieTiers.getNom() + " - "
                                    + getTransaction().getSession().getLabel("DT_AUNCUNE_PERIODE_AFFILIE"));
                        } else if (nbAffiliation > 1) {
                            dec.setNomAffilie(affilieTiers.getNom() + " - "
                                    + getTransaction().getSession().getLabel("DT_PLUSIEURS_PERIODES_AFFILIE"));
                        } else {
                            throw new Exception();
                        }
                    }

                } catch (Exception e) {
                    dec.setNomAffilie(getTransaction().getSession().getLabel("DT_PROB_PERIODES_AFFILIE"));
                }
            }
        }
        // periode d'affiliation

        dec.setNumeroAffilie(numeroAffilie);
        dec.setNumeroAvs(noAvs);
        dec.setNomPrenom(nomPrenom);
        dec.setAnnee(annee);
        dec.setMoisDebut(moisDebut);
        dec.setMoisFin(moisFin);
        dec.setMontantEcr(montantEcr);
        dec.setMontantPositif(montantPositif);

        return dec;
    }

    private AFAffiliation _getAffiliation(globaz.globall.db.BTransaction transaction, String noAffilie, String annee,
            int nb) throws Exception {
        String affiliationRetour = "";

        AFAffiliation affiliationRet = null;
        try {
            int anneeControle = Integer.parseInt(annee);
            globaz.naos.db.affiliation.AFAffiliationListViewBean vBean = new globaz.naos.db.affiliation.AFAffiliationListViewBean();
            vBean.setForAffilieNumero(noAffilie);
            vBean.setForIdTiers("");
            vBean.setSession(transaction.getSession());
            vBean.find(transaction);

            for (int i = 0; i < vBean.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) vBean.getEntity(i);

                int anneeDebAffiliation = globaz.globall.util.JACalendar.getYear(affiliation.getDateDebut());
                int anneeFinAffiliation = globaz.globall.util.JACalendar.getYear(affiliation.getDateFin());
                if (((anneeControle >= anneeDebAffiliation) && (anneeControle <= anneeFinAffiliation))
                        || ((anneeControle >= anneeDebAffiliation) && (anneeFinAffiliation == 0))) {
                    affiliationRet = affiliation;
                    nb++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nb > 1) {
            throw new Exception(getTransaction().getSession().getLabel("DT_PLUSIEURS_PERIODE_AFFILIE") + " ("
                    + noAffilie + "-" + annee + ")");

        } else {
            return affiliationRet;
        }
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

    /**
     * Returns the filename.
     * 
     * @return String
     */
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

        if (line != null) {
            // la ligne a déjà été lue, mas pas encore été retourné par next();
            return true;
        } else {
            // lecture de la prochaine ligne, (mis en cache pour que le prochain
            // next() n'aie
            // pas besoin de relire cette ligne)
            line = br.readLine();
            while ("".equals(line)) {
                line = br.readLine();
            }
            if (line != null) {
                // le fichier n'est pas terminé, on a trouvé une ligne
                return true;
            }
        }
        // fin du fichier, on a pas trouvé de ligne a lire.
        close();
        return false;
    }

    @Override
    public CIDeclarationRecord next() throws Exception {
        String tmpLine = null;
        if (!isReady) {
            _init();
        }
        try {
            // si il n'y a rien dans le cache, on lit dans le fichier
            if (line == null) {
                line = br.readLine();
                // saute les lignes vide
                while ("".equals(line)) {
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
        line = null;
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
                size = 0;
                FileInputStream sfi = new FileInputStream(getFilename());
                BufferedReader sbr = new BufferedReader(new InputStreamReader(sfi));
                while (sbr.readLine() != null) {
                    size++;
                }
                sfi.close();
                sbr.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            size = -1;
        }
        return size;

    }

}
