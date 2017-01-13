/*
 * Créé le 23 août 07
 */
package globaz.corvus.process;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.itext.REListeAdrPaiementRentesEnErreur;
import globaz.corvus.itext.REListeRecapitulativePaiement;
import globaz.corvus.itext.REListeRecapitulativePaiementPC_RFM;
import globaz.corvus.itext.REListeRentesDoubles;
import globaz.corvus.itext.REListeRentesEnErreur;
import globaz.corvus.itext.REListeRetenuesBlocages;
import globaz.corvus.itext.RERecapitulationPaiementAdapter;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.List;
import ch.globaz.common.properties.CommonProperties;

/**
 * <p>
 * Process effectuant la génération des documents de vérification du paiement mensuel
 * </p>
 * <p>
 * Ce process va également mettre à jour les flag <code>isAtteteMajRetenue/Blocage</code> sur les rentes accordées. Pour
 * cette raison, si le lot se trouve dans l'état PARTIEL, VALIDE ou ERREUR ce traitement ne peut s'effectuer, faute de
 * quoi des écritures de retenues/blocage pourraient être traitées à double.
 * </p>
 * 
 * <p>
 * Les documents suivants sont générés :
 * </p>
 * 
 * <ul>
 * <li>Liste des retenues</li>
 * <li>Liste récapitulative du paiement</li>
 * <li>Liste des rentes doubles</li>
 * <li>Liste des rentes en erreur</li>
 * </ul>
 * 
 * @author BSC
 * @author FGO
 * @author SCR
 * 
 */
public class REGenererListesVerificationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String LISTE_ADRESSE_PAIEMENT_ERREUR_ORDER = "05";
    public static final String LISTE_RECAP_PAIEM_ORDER = "02";
    public static final String LISTE_RENTES_DOUBLES_ORDER = "03";
    public static final String LISTE_RENTES_ERREUR_ORDER = "04";
    public static final String LISTE_RETENUES_ORDER = "01";
    public static final String PROPERTY_DOCUMENT_ORDER = "DOCUMENT_ORDER";

    private String emailObject = "";
    private REListeRecapitulativePaiement listeRecapitulativePaiement = null;
    private REListeRecapitulativePaiementPC_RFM listeRecapitulativePaiementPCRFM = null;
    private REListeRentesDoubles listeRentesDoubles = null;
    private REListeRentesEnErreur listeRentesEnErreur = null;
    private REListeRetenuesBlocages listeRetenues = null;
    private String moisAnnee = "";

    private long start;
    private long stop;

    public REGenererListesVerificationProcess() {
        super();
    }

    public REGenererListesVerificationProcess(BProcess parent) {
        super(parent);
    }

    public REGenererListesVerificationProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean succes = false;
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        RELot lot = null;
        try {
            _validate();
            if (getTransaction().hasErrors() || getSession().hasErrors()) {
                succes = false;
                throw new Exception("Validation failed !!!");
            }
            RELotManager mgrl = new RELotManager();
            mgrl.setSession(getSession());
            mgrl.setForCsEtatIn(IRELot.CS_ETAT_LOT_VALIDE + ", " + IRELot.CS_ETAT_LOT_PARTIEL + ", "
                    + IRELot.CS_ETAT_LOT_ERREUR);
            mgrl.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
            mgrl.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            mgrl.setForDateEnvoiInMMxAAAA(getMoisAnnee());
            mgrl.find(transaction, 1);

            if (mgrl.size() > 0) {
                throw new Exception("La préparation de ce mois à déjà été effectuée.");
            } else {
                mgrl.setForCsEtatIn(IRELot.CS_ETAT_LOT_OUVERT);
                mgrl.find(transaction, 1);

                if (mgrl.size() == 0) {
                    // Création du lot, dans l'état ouvert
                    lot = new RELot();
                    lot.setSession(getSession());
                    lot.setCsTypeLot(IRELot.CS_TYP_LOT_MENSUEL);
                    lot.setDescription(getSession().getLabel("PMT_MENS_RENTES"));
                    lot.setCsEtatLot(IRELot.CS_ETAT_LOT_OUVERT);
                    lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
                    lot.setDateCreationLot(JACalendar.todayJJsMMsAAAA());
                    lot.setDateEnvoiLot("01." + getMoisAnnee());
                    lot.add(transaction);
                } else {
                    lot = (RELot) mgrl.getFirstEntity();
                }
            }

            getMemoryLog().setTransaction(transaction);

            // Il est nécessaire de faire prioritairement ce traitement,
            // Car toutes les rentes échues seront flaggées avec isPrestationBloquee = TRUE.
            // Ainsi, elles apparaitront également dans la liste des rentes bloquées.
            startChrono();
            doMiseAJourRentesEchuesProcess(transaction);
            stopChrono();
            getMemoryLog().logMessage("Mise à jours des rentes échues: " + getChrono() + " secondes",
                    FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la mise à jour du flag des retenues");
            }
            // Code commenté car le point a WEBAVS-1988 a du être retiré
            // startChrono();
            // List<RetenueMontantTotalCorrige> retenues = doUpdateRetenueIfNeeded(transaction);
            // stopChrono();
            //
            // getMemoryLog().logMessage(
            // FWMessageFormat.format(getSession().getLabel("LIST_VERIFICATION_RENTE_RETENUE_MODIFIER_INFO"),
            // getChrono()), FWMessage.INFORMATION, "");
            // for (RetenueMontantTotalCorrige retenue : retenues) {
            //
            // getMemoryLog().logMessage(
            // FWMessageFormat.format(getSession().getLabel("LIST_VERIFICATION_RENTE_RETENUE_MODIFIER"),
            // retenue.getNss() + " / " + retenue.getDesignation(), retenue.getMontantTotalARetenir(),
            // retenue.getMontantTotaleAretenirCorriger(), retenue.getIdExterneRole(),
            // retenue.getSection()), FWMessage.INFORMATION, "");
            // }

            // ///////////////////////////////////////////////////////////////////////////////////////
            // Creation des documents de validation
            // ///////////////////////////////////////////////////////////////////////////////////////

            startChrono();
            doListeRentesDoubles(transaction);

            stopChrono();
            getMemoryLog().logMessage("Liste des rentes doubles: " + getChrono() + " secondes", FWMessage.INFORMATION,
                    "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la liste des rentes doubles");
            }

            startChrono();
            doListeRentesEnErreur(transaction);
            stopChrono();
            getMemoryLog().logMessage("Liste des rentes en erreur: " + getChrono() + " secondes",
                    FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la liste des rentes en erreur");
            }

            startChrono();
            doListeAdrPaiementRentesEnErreur(transaction);
            stopChrono();
            getMemoryLog().logMessage("Liste des adresses de paiement en erreur : " + getChrono() + " secondes",
                    FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception(
                        "Erreur dans la génération de la liste des adresse de paiement des rentes en erreur");
            }

            // Maj des ra avec le flag isRetenue
            startChrono();
            doMiseAJourRetenuesProcess(transaction);
            stopChrono();
            getMemoryLog().logMessage(
                    "Mise à jours des rentes accordées avec le flag isRetenue: " + getChrono() + " secondes",
                    FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la mise à jour du flag des retenues");
            }

            // /////////////////////////////////////////////////////////////////////////////////////
            // on fait les listes recapitulative et des retenues apres la mise a jour du flag isRetenues
            // /////////////////////////////////////////////////////////////////////////////////////

            startChrono();
            doListeRetenues(transaction);
            stopChrono();
            getMemoryLog().logMessage("Liste des retenues: " + getChrono() + " secondes", FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la liste des retenues");
            }

            startChrono();
            doListeRecapitulativePaiement(transaction);
            stopChrono();
            getMemoryLog().logMessage("Liste récapitulation des paiements: " + getChrono() + " secondes",
                    FWMessage.INFORMATION, "");
            if (getMemoryLog().hasErrors()) {
                throw new Exception("Erreur dans la génération de la liste récapitulative du paiement");
            }

            transaction.commit();
            succes = true;
        } catch (Exception e) {

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(session.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
            }

            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {

            if (succes) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GEN_LISTES_VERIFS_SUCCES");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_GEN_LISTES_VERIFS_ERREUR");
            }
            try {
                // Fusion des documents
                JadePublishDocumentInfo info = createDocumentInfo();
                info.setDocumentType(IRENoDocumentInfoRom.LISTE_RECAPITULATIVE_DU_PAIEMENT);
                info.setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RECAPITULATIVE_DU_PAIEMENT);
                info.setDocumentProperty("corvus.verif.periode", moisAnnee);
                info.setPublishDocument(true);
                info.setArchiveDocument(true);

                this.mergePDF(info, true, 500, false, REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER);

                transaction.closeTransaction();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void _validate() throws Exception {
        JADate mmAAAA = new JADate(getMoisAnnee());
        JADate dateProchainPmt = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
        JACalendar cal = new JACalendarGregorian();
        dateProchainPmt = cal.addMonths(dateProchainPmt, 1);
        if (cal.compare(mmAAAA, dateProchainPmt) != JACalendar.COMPARE_EQUALS) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_EGALE_PROCHAIN_PMT_MENS"));
        }
    }

    /**
     * Lancement du process de mise à jour du flag dans les rentes accordées
     * 
     * @param transaction
     * @throws Exception
     */
    private void doListeAdrPaiementRentesEnErreur(BTransaction transaction) throws Exception {
        REListeAdrPaiementRentesEnErreur liste = new REListeAdrPaiementRentesEnErreur(transaction.getSession());
        liste.setForMoisAnnee(moisAnnee);
        liste.setParentWithCopy(this);
        liste.setTransaction(transaction);
        liste.executeProcess();
    }

    private void doListeRecapitulativePaiement(BITransaction transaction) throws Exception {

        // On charge l'adapter
        RERecapitulationPaiementAdapter adapter = new RERecapitulationPaiementAdapter(getSession(), moisAnnee);
        adapter.chargerParGenrePrestation();

        listeRecapitulativePaiement = new REListeRecapitulativePaiement();
        listeRecapitulativePaiement.setForMoisAnnee(moisAnnee);
        listeRecapitulativePaiement.setTransaction(transaction);
        listeRecapitulativePaiement.setParentWithCopy(this);
        listeRecapitulativePaiement.setAdapter(adapter);
        listeRecapitulativePaiement.executeProcess();

        // Toutes les caisses n'ont pas de PC/RFM !!!
        if (adapter.getNbTotal2General() > 0) {
            listeRecapitulativePaiementPCRFM = new REListeRecapitulativePaiementPC_RFM();
            listeRecapitulativePaiementPCRFM.setForMoisAnnee(moisAnnee);
            listeRecapitulativePaiementPCRFM.setTransaction(transaction);
            listeRecapitulativePaiementPCRFM.setParentWithCopy(this);
            listeRecapitulativePaiementPCRFM.setAdapter(adapter);
            listeRecapitulativePaiementPCRFM.executeProcess();
        }
    }

    private void doListeRentesDoubles(BITransaction transaction) throws Exception {
        listeRentesDoubles = new REListeRentesDoubles();

        listeRentesDoubles.setMois(moisAnnee);
        listeRentesDoubles.setTransaction(transaction);
        listeRentesDoubles.setParentWithCopy(this);
        listeRentesDoubles.executeProcess();
    }

    private void doListeRentesEnErreur(BITransaction transaction) throws Exception {
        listeRentesEnErreur = new REListeRentesEnErreur();

        listeRentesEnErreur.setForMoisAnnee(moisAnnee);
        listeRentesEnErreur.setParentWithCopy(this);
        listeRentesEnErreur.setTransaction(transaction);
        listeRentesEnErreur.executeProcess();
    }

    private List<RetenueMontantTotalCorrige> doUpdateRetenueIfNeeded(BITransaction transaction) throws Exception {
        REMiseAJourRetenuesMontantProcess miseAJourRetenuesMontantProcess = new REMiseAJourRetenuesMontantProcess(
                getSession(), transaction);
        List<RetenueMontantTotalCorrige> retenuesMontantTotalCorrige = miseAJourRetenuesMontantProcess
                .verifRetenueAndChangeSolde();
        return retenuesMontantTotalCorrige;
    }

    private void doListeRetenues(BITransaction transaction) throws Exception {
        listeRetenues = new REListeRetenuesBlocages(getSession());
        listeRetenues.setMois(moisAnnee);
        listeRetenues.setTransaction(transaction);
        listeRetenues.setParentWithCopy(this);
        listeRetenues.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        listeRetenues.executeProcess();
    }

    private void doMiseAJourRentesEchuesProcess(BTransaction transaction) throws Exception {
        REMiseAJourRentesEchuesProcess process = new REMiseAJourRentesEchuesProcess(getSession());
        process.setEMailAddress(getEMailAddress());
        process.setParentWithCopy(this);
        process.setTransaction(transaction);
        process.executeProcess();
    }

    /**
     * Lancement de la liste des adresses de paiement en erreur
     * 
     * @param transaction
     * @throws Exception
     */
    private void doMiseAJourRetenuesProcess(BTransaction transaction) throws Exception {
        REMiseAJourRetenuesProcess process = new REMiseAJourRetenuesProcess(getSession());
        process.setEMailAddress(getEMailAddress());
        process.setParentWithCopy(this);
        process.setTransaction(transaction);
        process.executeProcess();
    }

    private float getChrono() {
        return (float) (stop - start) / (float) 1000;
    }

    public String getEmailObject() {
        return emailObject;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setEmailObject(String string) {
        emailObject = string;
    }

    public void setMoisAnnee(String string) {
        moisAnnee = string;
    }

    private void startChrono() {
        start = System.currentTimeMillis();
    }

    private void stopChrono() {
        stop = System.currentTimeMillis();
    }
}
