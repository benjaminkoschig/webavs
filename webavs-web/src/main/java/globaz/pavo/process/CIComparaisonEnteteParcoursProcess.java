package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.comparaison.CIAnomalieCI;
import globaz.pavo.db.comparaison.CIComparaisonIteratorInput;
import globaz.pavo.db.comparaison.CIComparaisonIteratorInputEBC;
import globaz.pavo.db.comparaison.CIComparaisonIteratorInputXML;
import globaz.pavo.db.comparaison.CICompteIndividuelComparaison;
import globaz.pavo.db.comparaison.CICompteIndividuelComparaisonManager;
import globaz.pavo.db.comparaison.CIEnteteRecord;
import globaz.pavo.db.comparaison.ICIComparaisonIteratorInput;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;

public class CIComparaisonEnteteParcoursProcess extends FWProcess {

    private static final long serialVersionUID = 2109313091249429101L;
    public final static String REFERENCE_INTERNE = "Zas Ableich";
    private ArrayList codesAComparer = new ArrayList();
    private ArrayList codesACorriger = new ArrayList();
    private CIRassemblementOuverture derniereCloRaou = null;
    private String fileName = new String();
    private String isEBCDIC = "false";
    private int nbErreur = 0;
    private boolean wantCompareAnneeOuverture = false;
    private boolean wantCompareCloture = false;
    private boolean wantCompareClotureCaisse = false;
    private boolean wantCompareClotureMotif = false;
    private boolean wantCompareEtatOrigine = false;
    // Paramètres sur les données à comparer
    private boolean wantCompareMotifOuverture = false;
    private boolean wantCompareNom = false;

    private boolean wantCompareNumeroAvsPrecedant = false;

    public CIComparaisonEnteteParcoursProcess() {
        super();
    }

    public CIComparaisonEnteteParcoursProcess(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public CIComparaisonEnteteParcoursProcess(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() {
        ICIComparaisonIteratorInput iterator = null;
        try {
            if (CIUtil.isAnnonceXML(getSession())) {
                iterator = new CIComparaisonIteratorInputXML();
            } else if (!CIUtil.isComparaisonEBCDIC(getSession())) {
                iterator = new CIComparaisonIteratorInput();
            } else {
                iterator = new CIComparaisonIteratorInputEBC();
            }

        } catch (Exception e1) {
            JadeLogger.debug(this, e1);
        }

        iterator.setFileName(getFileNameInput());
        int taille = 0;
        try {
            taille = iterator.size();
        } catch (Exception e) {
            taille = 0;
        }
        if (taille == 0 || taille == -1) {
            abort();
            return false;
        }
        setProgressScaleValue(taille);
        CICompteIndividuelComparaisonManager ciMgrTest = new CICompteIndividuelComparaisonManager();
        ciMgrTest.setSession(getSession());
        try {
            if (ciMgrTest.getCount() < 1) {
                abort();
                return false;
            }
        } catch (Exception e) {
            abort();
            return false;
        }

        CIEnteteRecord record = null;
        int nbCI = 0;
        int nbCiErreur = 0;
        long time = System.currentTimeMillis();
        boolean ok = false;
        try {
            codesACorriger = CIUtil.getCodesACorriger();
            initBooleanForCompare();
            while (iterator.hasNext()) {
                nbCI++;
                setProgressCounter(nbCI);
                try {
                    record = null;
                    record = iterator.next();
                    ok = isIdentique(record);
                    if (!ok) {
                        nbCiErreur++;
                    }
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    } else {
                        getTransaction().rollback();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.toString() + " ligne : " + Integer.toString(nbCI), FWMessage.FATAL,
                            "comparaison des entete");
                    getTransaction().rollback();
                }
                if (nbCI % 1000 == 0) {

                    System.out.println(nbCI + " ci updated in " + (System.currentTimeMillis() - time) / 1000 + "sec.");
                }
                // Vu qu'on ne charge qu'une fois la cloture(perf.) il faut la
                // remettre à null
                derniereCloRaou = null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        // maintenant, on parcours la table pour voir les CIs qui n'ont pas été
        // traité
        // et qui auront une anomalie de type absent à la centrale

        try {

            CICompteIndividuelComparaisonManager ciMgr = new CICompteIndividuelComparaisonManager();
            CICompteIndividuelComparaison ciCaisse;
            ciMgr.setForSuspens("2");
            ciMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            ciMgr.setSession(getSession());
            ciMgr.find(getTransaction());
            for (int i = 0; i < ciMgr.size(); i++) {
                ciCaisse = (CICompteIndividuelComparaison) ciMgr.getEntity(i);
                nbCiErreur++;
                try {
                    insertAnomalieCIManquantZAS(ciCaisse);
                    ciCaisse.traiteCi(getTransaction());
                    nbCiErreur++;
                    nbErreur++;
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    } else {
                        getTransaction().rollback();
                    }

                } catch (Exception e) {
                    getMemoryLog().logMessage("No : " + ciCaisse.getNumeroAvs(), FWMessage.FATAL,
                            "traitement des ano. absent ZAS");
                    getTransaction().rollback();
                }

            }
        } catch (Exception e) {
        }
        return !isAborted();
    }

    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    public String codeUtilisateurToCodeSysteme(BTransaction transaction, String code, String groupe) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    private boolean compareAnneeOuverture(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse)
            throws Exception {
        String anneeCompare = "";
        if (!JadeStringUtil.isIntegerEmpty(ciCaisse.getAnneeOuverture())) {
            anneeCompare = ciCaisse.getAnneeOuverture().trim();
        }
        if (record.getAnneeOuverture().trim().equals(anneeCompare)) {
            return true;
        } else {
            insertAnomalie(record, ciCaisse, CIAnomalieCI.CS_ANNEE_OUVERTURE);
            nbErreur++;
            return false;
        }
    }

    /**
     * @param record
     * @return
     * @throws Exception
     */
    // retourne faux si le CI n'existe pas au RA
    // retourne vrai si le ci existe mais qu'il est cloturé
    private boolean compareBooleanOUverture(CIEnteteRecord record) throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.wantCallMethodAfter(false);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(record.getNumeroAssure());
        ciMgr.find(getTransaction());
        if (ciMgr.size() == 0) {
            return false;
        } else {

            return true;

        }

    }

    private boolean compareClotureDate(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse) throws Exception {
        String dateCompare = "";
        boolean boolRetour = true;
        boolean foundRaou = true;
        if (!JadeStringUtil.isIntegerEmpty(ciCaisse.getDerniereCloture())) {
            dateCompare = ciCaisse.getDerniereCloture().trim();
        } else {
            derniereCloRaou = ciCaisse.getDerniereBCloture(getTransaction(), ciCaisse.getCompteIndividuelId());
            if (derniereCloRaou != null) {
                dateCompare = derniereCloRaou.getDateCloture();

            } else {
                foundRaou = false;
            }
        }
        String dateRec = record.getDateClotureFormatee();
        // if(record.getDateClotureFormatee().trim().equals(dateCompare)){
        if (!BSessionUtil.compareDateEqual(getSession(), dateCompare, dateRec)) {
            // erreur sur la cloture, pas besoin de continuer
            return false;
        }

        String caisseCompare = "";
        // Le FW retourne 0, donc on met le motif de comparaison à vide
        if (!JadeStringUtil.isIntegerEmpty(ciCaisse.getDerniereCaisse().trim())) {
            caisseCompare = ciCaisse.getCaisseAgenceFormatee().trim();
        } else {
            // on va voir s'il y a qqch dans le raou
            if (derniereCloRaou == null && foundRaou) {
                derniereCloRaou = ciCaisse.getDerniereBCloture(getTransaction(), ciCaisse.getCompteIndividuelId());
            }

            if (derniereCloRaou != null) {
                caisseCompare = derniereCloRaou.getCaisseAgenceCommettante();
            }
        }
        if (!record.getCaisseClotureFormatee().trim().equals(caisseCompare)) {
            return false;
        }

        return true;
    }

    private boolean compareEtatOrigine(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse) throws Exception {
        if (record.getPays().trim().equals(ciCaisse.getPays().trim())) {
            return true;
        } else {
            insertAnomalie(record, ciCaisse, CIAnomalieCI.CS_NATIONNALITE);
            nbErreur++;
            return false;
        }
    }

    private boolean compareMotifOuverture(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse)
            throws Exception {
        String motifCompare = "";
        if (!JadeStringUtil.isIntegerEmpty(ciCaisse.getDernierMotifOuverture())) {
            motifCompare = ciCaisse.getDernierMotifOuverture().trim();
        }
        if (record.getMotifOuverture().trim().equals(motifCompare)) {
            return true;
        } else {
            insertAnomalie(record, ciCaisse, CIAnomalieCI.CS_MOTIF_OUVERTURE);
            nbErreur++;
            return false;

        }

    }

    private boolean compareNom(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse) throws Exception {
        if (record.getNomPrenom().trim().equals(ciCaisse.getNomPrenom())) {
            return true;
        } else {
            insertAnomalie(record, ciCaisse, CIAnomalieCI.CS_NOM);
            nbErreur++;
            return false;
        }
    }

    private boolean compareNumeroAvsPrecedent(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse)
            throws Exception {
        if (record.getNumeroAssureAnterieur().trim().equals(ciCaisse.getNumeroAvsPrecedant().trim())) {
            return true;
        } else {
            insertAnomalie(record, ciCaisse, CIAnomalieCI.CS_NUMERO_AVS_ANCIEN);
            nbErreur++;
            return false;
        }
    }

    private String getCaisseAgence(String caisse, String agence) {
        if (!JadeStringUtil.isIntegerEmpty(agence)) {
            return caisse + "." + agence;
        } else {
            return caisse;
        }
    }

    @Override
    protected String getEMailObject() {
        if (!isAborted()) {
            return "la comparaison des entetes ci s'est effectuée avec succès";
        } else {
            return "la comparaison des entetes ci a échouée!";
        }
    }

    /**
     * @return
     */
    public String getFileNameInput() {
        String file = null;
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);

            if (JadeStringUtil.isBlank(application.getLocalPath())) {
                if (CIUtil.isAnnonceXML(getSession())) {
                    file = "./pavoRoot/enteteci" + application.getNumFichierEnteteCI() + ".xml";
                } else {
                    file = "./pavoRoot/comparaison.txt";
                }

            } else {
                if (CIUtil.isAnnonceXML(getSession())) {
                    file = application.getLocalPath() + "/pavoRoot/enteteci" + application.getNumFichierEnteteCI()
                            + ".xml";
                } else {
                    file = application.getLocalPath() + "/pavoRoot/comparaison.txt";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return file;
    }

    /**
     * @return
     */
    public String getIsEBCDIC() {
        return isEBCDIC;
    }

    public void initBooleanForCompare() {
        codesAComparer = CIUtil.getCodesAComparer();
        if (codesAComparer.indexOf(CIAnomalieCI.CS_ANNEE_OUVERTURE) >= 0) {
            setWantCompareAnneeOuverture(true);
        }
        if (codesAComparer.indexOf(CIAnomalieCI.CS_CLOTURE) >= 0) {
            setWantCompareCloture(true);
        }

        if (codesAComparer.indexOf(CIAnomalieCI.CS_MOTIF_OUVERTURE) >= 0) {
            setWantCompareMotifOuverture(true);
        }
        if (codesAComparer.indexOf(CIAnomalieCI.CS_NOM) >= 0) {
            setWantCompareNom(true);
        }
        if (codesAComparer.indexOf(CIAnomalieCI.CS_NATIONNALITE) >= 0) {
            setWantCompareEtatOrigine(true);
        }
        if (codesAComparer.indexOf(CIAnomalieCI.CS_NUMERO_AVS_ANCIEN) >= 0) {
            setWantCompareNumeroAvsPrecedant(true);
        }

    }

    /**
     * Insére l'anomalie en fonction du record 24 et du CI au RA
     * 
     * @param record
     * @param ciCaisse
     * @throws Exception
     */
    private void insertAnomalie(CIEnteteRecord record, CICompteIndividuelComparaison ciCaisse, String typeAnomalie)
            throws Exception {
        CIAnomalieCI anom = new CIAnomalieCI();
        try {
            anom.setCompteIndividuelId(ciCaisse.getCompteIndividuelId());
        } catch (Exception e) {
        }
        anom.setNumeroAvs(record.getNumeroAssure());
        anom.setNumeroAvsPrecedent(record.getNumeroAssureAnterieur());
        anom.setNomPrenom(record.getNomPrenom());
        anom.setPays(codeUtilisateurToCodeSysteme(getTransaction(), record.getPays(), "CIPAYORI"));
        anom.setMotifOuverture(record.getMotifOuverture());
        anom.setAnneeOuverture(record.getAnneeOuverture());
        anom.setDernierMotif(record.getMotifRci());
        anom.setDateCloture(record.getDateClotureFormatee());
        /************************************************************
         * ATTENTION CONVERSION CAISSE AGENCE
         ***********************************************************/
        anom.setDerniereCaisse(getCaisseAgence(record.getCaisseCommettante(), record.getAgenceCommettante()));
        anom.setTypeAnomalie(typeAnomalie);
        // ajouter les code a traiter si dans propertie
        if (codesACorriger.indexOf(anom.getTypeAnomalie()) >= 0
                && !CIAnomalieCI.CS_CLOTURE.equals(anom.getTypeAnomalie())) {
            anom.setEtat(CIAnomalieCI.CS_A_TRAITER);
        } else {
            anom.setEtat(CIAnomalieCI.CS_NE_PAS_TRAITER);
        }
        anom.add(getTransaction());

    }

    private void insertAnomalieCIManquantZAS(CICompteIndividuelComparaison ciCaisse) throws Exception {
        CIAnomalieCI anom = new CIAnomalieCI();
        anom.setNumeroAvs(ciCaisse.getNumeroAvs());
        anom.setCompteIndividuelId(ciCaisse.getCompteIndividuelId());
        anom.setNumeroAvsPrecedent(ciCaisse.getNumeroAvsPrecedant());
        anom.setNomPrenom(ciCaisse.getNomPrenom());
        anom.setPays(ciCaisse.getPaysOrigineId());
        anom.setMotifOuverture(ciCaisse.getDernierMotifOuverture());
        anom.setAnneeOuverture(ciCaisse.getAnneeOuverture());
        anom.setDernierMotif(ciCaisse.getDernierMotifCloture());
        anom.setDateCloture(ciCaisse.getDerniereCloture());
        /************************************************************
         * ATTENTION CONVERSION CAISSE AGENCE
         ***********************************************************/
        anom.setDerniereCaisse(ciCaisse.getCaisseAgenceFormatee());
        anom.setTypeAnomalie(CIAnomalieCI.CS_CI_ABSENT_A_ZAS);
        if (codesACorriger.indexOf(anom.getTypeAnomalie()) >= 0) {
            anom.setEtat(CIAnomalieCI.CS_A_TRAITER);
        } else {
            anom.setEtat(CIAnomalieCI.CS_NE_PAS_TRAITER);
        }
        anom.add(getTransaction());

    }

    /**
     * @param rec
     * @return
     * @throws Exception
     */
    private boolean isIdentique(CIEnteteRecord rec) throws Exception {
        // retour global de la méthode
        boolean retour = true;
        // retour pour chaque méthode appelée
        boolean retourInter = true;
        CICompteIndividuelComparaisonManager ciMgr = new CICompteIndividuelComparaisonManager();
        CICompteIndividuelComparaison ciCaisse = null;
        ciMgr.setSession(getSession());
        ciMgr.setForNumeroAvs(rec.getNumeroAssure());
        ciMgr.find(getTransaction());
        if (ciMgr.size() > 1) {
            getMemoryLog().logMessage("Plusieurs CIs existent pour cet assuré : " + rec.getNumeroAssure() + " ",
                    FWMessage.FATAL, "comparaison des entete");
            return false;
        }
        if (ciMgr.size() == 0) {
            if (!compareBooleanOUverture(rec)) {
                insertAnomalie(rec, ciCaisse, CIAnomalieCI.CS_CI_ABSENT_A_LA_CAISSE);
                // si le CI est clôturé mais présent, il faut quand même
                // continuer la comparaison
                // retourInter = false;
                nbErreur++;
                return false;

            } else {
                insertAnomalie(rec, ciCaisse, CIAnomalieCI.CS_CI_PRESENT_CLOTURE);
                // Si le Ci est absent, => return car on ne peut pas comparer le
                // CI
                nbErreur++;
                return false;

            }

        } else {

            ciCaisse = (CICompteIndividuelComparaison) ciMgr.getFirstEntity();
            // on met le CI à traité
            ciCaisse.traiteCi(getTransaction());
            if (wantCompareMotifOuverture) {
                retourInter = compareMotifOuverture(rec, ciCaisse);
                if (retourInter == false) {
                    retour = retourInter;
                }
            }
            if (wantCompareAnneeOuverture) {
                retourInter = compareAnneeOuverture(rec, ciCaisse);
                if (retourInter == false) {
                    retour = retourInter;
                }
            }
            if (wantCompareNom) {
                retourInter = compareNom(rec, ciCaisse);
                if (retourInter == false) {
                    retour = retourInter;
                }
            }
            if (wantCompareNumeroAvsPrecedant) {
                retourInter = compareNumeroAvsPrecedent(rec, ciCaisse);
                if (retourInter == false) {
                    retour = retourInter;
                }
            }
            if (wantCompareEtatOrigine) {
                retourInter = compareEtatOrigine(rec, ciCaisse);
                if (retourInter == false) {
                    retour = retourInter;
                }
            }
            if (wantCompareCloture) {
                if (!compareClotureDate(rec, ciCaisse)) {
                    retourInter = false;
                    insertAnomalie(rec, ciCaisse, CIAnomalieCI.CS_CLOTURE);
                }

                if (retourInter == false) {
                    retour = retourInter;

                }

            }

        }
        return retour;
    }

    /**
     * @return
     */
    public boolean isWantCompareAnneeOuverture() {
        return wantCompareAnneeOuverture;
    }

    /**
     * @return
     */
    public boolean isWantCompareCloture() {
        return wantCompareCloture;
    }

    /**
     * @return
     */
    public boolean isWantCompareClotureCaisse() {
        return wantCompareClotureCaisse;
    }

    /**
     * @return
     */
    public boolean isWantCompareClotureMotif() {
        return wantCompareClotureMotif;
    }

    /**
     * @return
     */
    public boolean isWantCompareEtatOrigine() {
        return wantCompareEtatOrigine;
    }

    /**
     * @return
     */
    public boolean isWantCompareMotifOuverture() {
        return wantCompareMotifOuverture;
    }

    /**
     * @return
     */
    public boolean isWantCompareNom() {
        return wantCompareNom;
    }

    /**
     * @return
     */
    public boolean isWantCompareNumeroAvsPrecedant() {
        return wantCompareNumeroAvsPrecedant;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setFileName(String string) {
        fileName = string;
    }

    /**
     * @param string
     */
    public void setIsEBCDIC(String string) {
        isEBCDIC = string;
    }

    /**
     * @param b
     */
    public void setWantCompareAnneeOuverture(boolean b) {
        wantCompareAnneeOuverture = b;
    }

    /**
     * @param b
     */
    public void setWantCompareCloture(boolean b) {
        wantCompareCloture = b;
    }

    /**
     * @param b
     */
    public void setWantCompareClotureCaisse(boolean b) {
        wantCompareClotureCaisse = b;
    }

    /**
     * @param b
     */
    public void setWantCompareClotureMotif(boolean b) {
        wantCompareClotureMotif = b;
    }

    /**
     * @param b
     */
    public void setWantCompareEtatOrigine(boolean b) {
        wantCompareEtatOrigine = b;
    }

    /**
     * @param b
     */
    public void setWantCompareMotifOuverture(boolean b) {
        wantCompareMotifOuverture = b;
    }

    /**
     * @param b
     */
    public void setWantCompareNom(boolean b) {
        wantCompareNom = b;
    }

    /**
     * @param b
     */
    public void setWantCompareNumeroAvsPrecedant(boolean b) {
        wantCompareNumeroAvsPrecedant = b;
    }

}
