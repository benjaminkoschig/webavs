/*
 * Créé le 6 mars 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.inscriptions.declaration;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class CIDeclarationParametreIterator implements ICIDeclarationIterator {

    private BufferedReader br = null;
    private FileInputStream fi = null;
    private String filename = "";
    private boolean isReady = false;
    // structaure d'un élement a lire (une ligne)
    private String line = null; // doir être initaialisé a null !
    private CIDeclarationParametre params = null;
    private String provenance = "";
    BSession session = null;
    private int size = -1;

    private TreeMap<?, ?> tableNom = new TreeMap<Object, Object>();

    private BTransaction transaction = null;

    private String typeImport = "";

    private CIDeclarationRecord _fillBean(String line) {

        if (params == null) {
            params = new CIDeclarationParametre();
            try {
                params.init(transaction.getSession(), typeImport);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // champs
        String numeroAffilie = line.substring(conv(params.getNoAffillieDebut()), conv(params.getNoAffillieFin()));
        if (!JadeStringUtil.isBlank(params.getNoAffillieSuiteDebut())
                && !JadeStringUtil.isBlank(params.getNoAffillieSuiteFin())) {
            String noAffilieSuite = line.substring(conv(params.getNoAffillieSuiteDebut()),
                    conv(params.getNoAffillieSuiteFin()));
            numeroAffilie += noAffilieSuite;
        }
        String numeroAffilieFormate = "";
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            IFormatData affilieFormater = application.getAffileFormater();
            numeroAffilieFormate = affilieFormater.format(numeroAffilie);
        } catch (Exception e) {
        }

        String noAvs = line.substring(conv(params.getNoAvsDebut()), conv(params.getNoAvsFin()));
        if (!JadeStringUtil.isBlankOrZero(noAvs)) {
            if (noAvs.startsWith("-")) {
                if (NSUtil.unFormatAVS(noAvs).length() > 1) {
                    noAvs = "756" + noAvs.substring(1);
                }
            }
        }
        String nomPrenom = "";
        if (!JadeStringUtil.isBlank(params.getNomPrenomDebut()) && !JadeStringUtil.isBlank(params.getNomPrenomFin())) {
            nomPrenom = line.substring(conv(params.getNomPrenomDebut()), conv(params.getNomPrenomFin()));
        }

        String annee = line.substring(conv(params.getAnneeDebut()), conv(params.getAnneeFin()));
        if (annee.length() == 2) {
            if (Integer.parseInt(annee) > 48) {
                annee = "19" + annee;
            } else {
                annee = "20" + annee;
            }
        }
        int moisDebut = Integer.parseInt(line.substring(conv(params.getMoisDebutDebut()),
                conv(params.getMoisDebutFin())));
        int moisFin = Integer.parseInt(line.substring(conv(params.getMoisFinDebut()), conv(params.getMoisFinFin())));
        boolean montantPositif = true;
        if (!JadeStringUtil.isBlank(params.getNegatifValue())) {
            if (params.getNegatifValue().equals(
                    line.substring(conv(params.getNegatif()), conv(params.getNegatif()) + 1))) {
                montantPositif = false;
            }
        }

        String montantEcr = "";
        if ("false".equalsIgnoreCase(params.getAvecPoint())) {
            if ("false".equalsIgnoreCase(params.getAvecCentime())) {
                montantEcr = line.substring(conv(params.getMontantDebut()), conv(params.getMontantFin())) + ".00";
            } else {
                montantEcr = line.substring(conv(params.getMontantDebut()), conv(params.getMontantFin()) - 2) + "."
                        + line.substring(conv(params.getMontantFin()) - 2, conv(params.getMontantFin()));
            }

        } else {
            montantEcr = line.substring(conv(params.getMontantDebut()), conv(params.getMontantFin()));
        }
        montantEcr = montantEcr.trim();
        if (montantEcr.startsWith("-")) {
            montantPositif = false;
            montantEcr = montantEcr.substring(1);

        }

        int jourDebut = 0;
        int jourFin = 0;
        if (!JadeStringUtil.isBlank(params.getJourDebutDebut()) && !JadeStringUtil.isBlank(params.getJourDebutFin())) {
            jourDebut = conv(line.substring(conv(params.getJourDebutDebut()), conv(params.getJourDebutFin())));
        }
        if (!JadeStringUtil.isBlank(params.getJourFinDebut()) && !JadeStringUtil.isBlank(params.getJourFinFin())) {
            jourFin = conv(line.substring(conv(params.getJourFinDebut()), conv(params.getJourFinFin())));
        }

        String genre = "";
        if (!JadeStringUtil.isBlank(params.getGenreDebut()) && !JadeStringUtil.isBlank(params.getGenreFin())) {
            genre = line.substring(conv(params.getGenreDebut()), conv(params.getGenreFin()));
        }

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
                try {
                    int nbAffiliation = 0;
                    AFAffiliation affiliation = _getAffiliation(transaction, numeroAffilieFormate, annee, nbAffiliation);
                    if (affiliation != null) {
                        dec.setDebutAffiliation(affiliation.getDateDebut());
                        dec.setFinAffiliation(affiliation.getDateFin());
                        dec.setNomAffilie(affiliation.getTiersNom());
                        dec.setIdAffiliation(affiliation.getAffiliationId());

                    } else {
                        if (nbAffiliation == 0) {
                            dec.setNomAffilie(getTransaction().getSession().getLabel("DT_AUNCUNE_PERIODE_AFFILIE"));
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
        dec.setJourDebut(jourDebut);
        dec.setJourFin(jourFin);
        dec.setGenreEcriture(genre);

        return dec;
    }

    private AFAffiliation _getAffiliation(globaz.globall.db.BTransaction transaction, String noAffilie, String annee,
            int nb) throws Exception {

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

    private int conv(String str) {
        return Integer.parseInt(str);
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
    public TreeMap<String, Object> getNbSalaires() {
        return null;
    }

    @Override
    public TreeMap<?, ?> getNoAffiliePourReception() throws Exception {
        return null;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    @Override
    public TreeMap<String, Object> getTotauxJournaux() {
        return null;
    }

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

    @Override
    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;

    }

    @Override
    public void setTypeImport(String type) {
        typeImport = type;

    }

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
