package globaz.pavo.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import globaz.pavo.util.CIUtil;

/**
 * @author mmo
 * 
 */
public class CIAnnonceCentraleGenerationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String MODE_LANCEMENT_AUTOMATIQUE = "modeLancementAutomatique";
    public static final String MODE_LANCEMENT_MANUEL = "modeLancementManuel";

    private int anneeCourante;
    private CIAnnonceCentrale annonceCentrale;
    private String debutPeriodeEnvoi;
    private String finPeriodeEnvoi;
    private String modeLancement;
    CIAnnonceCentraleProcessXML annonceCentraleProcessXML;

    @Override
    protected void _executeCleanUp() {
        // Nothing to do
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean isProcessOnError = false;

        String mailDetailMessage = getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_MAIL_DETAIL_SUCCES");

        try {
            initProcessInputs();
            validateProcessInputs();
            annoncerInscriptionsACentrale();

            boolean isImpressionEnErreur = false;
            if (CIAnnonceCentrale.CS_ETAT_GENERE.equalsIgnoreCase(annonceCentrale.getIdEtat())
                    || (CIUtil.isAnnonceXML(getSession()) && CIAnnonceCentrale.CS_ETAT_ENVOYE
                            .equalsIgnoreCase(annonceCentrale.getIdEtat()))) {
                isImpressionEnErreur = imprimerProtocole();
            }

            ouvrirNouvelleAnnonce();

            if ((isAborted() || isOnError() || getSession().hasErrors()) || isImpressionEnErreur) {
                isProcessOnError = true;
                mailDetailMessage = getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_MAIL_DETAIL_ERREURS");
            }
        } catch (Exception e) {
            isProcessOnError = true;
            mailDetailMessage = getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_MAIL_DETAIL_ERREURS") + " : "
                    + e.toString();
        }

        getMemoryLog().logMessage(mailDetailMessage, FWMessage.INFORMATION, this.getClass().getName());

        return isProcessOnError;
    }

    private void annoncerInscriptionsACentrale() throws Exception {

        if (CIUtil.isAnnonceInscriptionXML(getSession())) {
            if (annonceCentraleProcessXML == null) {
                annonceCentraleProcessXML = new CIAnnonceCentraleProcessXML();
            }
            annonceCentraleProcessXML.setParentWithCopy(this);
            annonceCentraleProcessXML.setModeExecution(CIAnnonceCentraleProcess.MODE_EXECUTION_INFOROM_D0064);
            annonceCentraleProcessXML.setAnnonceCentrale(annonceCentrale);
            annonceCentraleProcessXML.executeProcess();
        } else {
            CIAnnonceCentraleProcess annonceCentraleProcess = new CIAnnonceCentraleProcess();
            annonceCentraleProcess.setParentWithCopy(this);
            annonceCentraleProcess.setModeExecution(CIAnnonceCentraleProcess.MODE_EXECUTION_INFOROM_D0064);
            annonceCentraleProcess.setAnnonceCentrale(annonceCentrale);
            annonceCentraleProcess.executeProcess();
        }
    }

    public int getAnneeCourante() {
        return anneeCourante;
    }

    public CIAnnonceCentrale getAnnonceCentrale() {
        return annonceCentrale;
    }

    public String getDebutPeriodeEnvoi() {
        return debutPeriodeEnvoi;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_MAIL_SUBJECT");
    }

    public String getFinPeriodeEnvoi() {
        return finPeriodeEnvoi;
    }

    @Override
    public boolean getForceCompletionMail() {
        // Dans tous les cas un mail est envoyé
        return true;
    }

    public String getModeLancement() {
        return modeLancement;
    }

    private boolean imprimerProtocole() throws Exception {

        CIAnnonceCentraleImpressionRapportProcess annonceCentraleImpressionRapportProcess = new CIAnnonceCentraleImpressionRapportProcess();
        annonceCentraleImpressionRapportProcess.setParentWithCopy(this);
        annonceCentraleImpressionRapportProcess.setAnnonceCentrale(annonceCentrale);
        annonceCentraleImpressionRapportProcess
                .setModeFonctionnement(CIAnnonceCentraleImpressionRapportProcess.MODE_FONCTIONNEMENT_IMPRESSION);
        annonceCentraleImpressionRapportProcess.executeProcess();

        return annonceCentraleImpressionRapportProcess.isProcessOnError();

    }

    private void initProcessInputs() throws Exception {

        if (CIAnnonceCentraleGenerationProcess.MODE_LANCEMENT_AUTOMATIQUE.equalsIgnoreCase(modeLancement)) {
            // rechercher l'annonce à générer
            String anneeMoisCourant = JACalendar.todayJJsMMsAAAA().substring(6, 10)
                    + JACalendar.todayJJsMMsAAAA().substring(3, 5);
            CIAnnonceCentraleManager annonceCentraleManager = new CIAnnonceCentraleManager();
            annonceCentraleManager.setSession(getSession());
            annonceCentraleManager.setForAnneeMoisCreation(anneeMoisCourant);
            annonceCentraleManager.find();

            if (annonceCentraleManager.size() == 1) {
                annonceCentrale = (CIAnnonceCentrale) annonceCentraleManager.getFirstEntity();
            }

        }

        anneeCourante = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());

        String joursMoisDebutPeriodeEnvoi = GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).getProperty("annonce.periode.debut");

        String joursMoisFinPeriodeEnvoi = GlobazServer.getCurrentSystem()
                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).getProperty("annonce.periode.fin");

        debutPeriodeEnvoi = joursMoisDebutPeriodeEnvoi + "." + anneeCourante;
        finPeriodeEnvoi = joursMoisFinPeriodeEnvoi + "." + anneeCourante;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void ouvrirNouvelleAnnonce() throws Exception {

        int moisAnnonce = JACalendar.getMonth(annonceCentrale.getDateCreation());
        int moisNewAnnonce = moisAnnonce + 1;
        int moisDebutPeriodeEnvoi = JACalendar.getMonth(debutPeriodeEnvoi);
        int moisFinPeriodeEnvoi = JACalendar.getMonth(finPeriodeEnvoi);

        if (moisNewAnnonce > 12) {
            moisNewAnnonce = 1;
        }

        if ((moisAnnonce < moisDebutPeriodeEnvoi) || (moisAnnonce > moisFinPeriodeEnvoi)) {
            // Annonce manuelle faite dans des cas exceptionnels sur autorisation de la centrale
            // L'ouverture automatique d'une nouvelle annonce ne doit donc pas être faite
            return;
        }

        String moisNewAnnonceString = JadeStringUtil.fillWithZeroes(String.valueOf(moisNewAnnonce), 2);
        String dateCreationNewAnnonce = "01." + moisNewAnnonceString + "." + anneeCourante;

        if (moisAnnonce == moisFinPeriodeEnvoi) {
            // il faut créer la première annonce de l'année suivante
            int anneeSuivante = anneeCourante + 1;
            String moisDebutPeriodeEnvoiString = JadeStringUtil
                    .fillWithZeroes(String.valueOf(moisDebutPeriodeEnvoi), 2);
            dateCreationNewAnnonce = "01." + moisDebutPeriodeEnvoiString + "." + anneeSuivante;

        }
        String dateEnvoiNewAnnonce = JadeDateUtil.getLastDateOfMonth(dateCreationNewAnnonce);

        String typeNewAnnonce = CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE;
        if (moisNewAnnonce == moisFinPeriodeEnvoi) {
            typeNewAnnonce = CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE;
        }

        CIAnnonceCentrale theNewAnnonceCentrale = new CIAnnonceCentrale();
        theNewAnnonceCentrale.setSession(getSession());
        theNewAnnonceCentrale.setDateCreation(dateCreationNewAnnonce);
        theNewAnnonceCentrale.setDateEnvoi(dateEnvoiNewAnnonce);
        theNewAnnonceCentrale.setIdTypeAnnonce(typeNewAnnonce);

        if (!theNewAnnonceCentrale.isDejaAnnonceExistante()) {
            theNewAnnonceCentrale.add(getTransaction());
        }

    }

    public void setAnneeCourante(int anneeCourante) {
        this.anneeCourante = anneeCourante;
    }

    public void setAnnonceCentrale(CIAnnonceCentrale annonceCentrale) {
        this.annonceCentrale = annonceCentrale;
    }

    public void setDebutPeriodeEnvoi(String debutPeriodeEnvoi) {
        this.debutPeriodeEnvoi = debutPeriodeEnvoi;
    }

    public void setFinPeriodeEnvoi(String finPeriodeEnvoi) {
        this.finPeriodeEnvoi = finPeriodeEnvoi;
    }

    public void setModeLancement(String modeLancement) {
        this.modeLancement = modeLancement;
    }

    private void validateProcessInputs() throws Exception {

        if ((annonceCentrale == null) || annonceCentrale.isNew()) {
            throw new Exception(getSession().getLabel(
                    "CI_ANNONCE_CENTRALE_GENERATION_ERREUR_ANNONCE_A_GENERER_EXISTE_PAS"));
        }

        StringBuffer wrongInputBuffer = new StringBuffer();
        String moisAnneeDateCourante = JACalendar.todayJJsMMsAAAA().substring(3, 10);
        String moisAnneeAnnonceAGenerer = annonceCentrale.getDateCreation().substring(3, 10);

        if (!JadeDateUtil.isGlobazDate(debutPeriodeEnvoi)) {
            wrongInputBuffer.append(getSession().getLabel(
                    "CI_ANNONCE_CENTRALE_ERREUR_PROPERTY_ANNONCE_PERIODE_DEBUT_MANDATORY")
                    + "\n");
        }

        if (!JadeDateUtil.isGlobazDate(finPeriodeEnvoi)) {
            wrongInputBuffer.append(getSession().getLabel(
                    "CI_ANNONCE_CENTRALE_ERREUR_PROPERTY_ANNONCE_PERIODE_FIN_MANDATORY")
                    + "\n");
        }

        if (!moisAnneeDateCourante.equalsIgnoreCase(moisAnneeAnnonceAGenerer)) {
            wrongInputBuffer.append(getSession().getLabel(
                    "CI_ANNONCE_CENTRALE_GENERATION_ERREUR_PAS_ANNONCE_DU_MOIS_EN_COURS")
                    + "\n");
        }

        if (!JadeStringUtil.isBlankOrZero(annonceCentrale.getIdEtat())) {
            wrongInputBuffer.append(getSession().getLabel(
                    "CI_ANNONCE_CENTRALE_GENERATION_ERREUR_ANNONCE_DEJA_GENEREE_OU_ENVOYEE")
                    + "\n");
        }

        if (wrongInputBuffer.length() >= 1) {
            throw new Exception(wrongInputBuffer.toString());
        }
    }

}
