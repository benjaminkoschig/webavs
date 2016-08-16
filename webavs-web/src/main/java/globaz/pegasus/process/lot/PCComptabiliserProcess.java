package globaz.pegasus.process.lot;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.pegasus.process.PCAbstractJob;
import globaz.pegasus.process.decision.PCImprimerDecisionsProcess;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler.PROCESS_TYPE;
import globaz.pegasus.utils.ChrysaorUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ComptabilisationParameter;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;

/**
 * Processus de comptabilisation PC
 * 
 * @author DMA
 * @author SCE (modifications mise en ged auto)
 * 
 */
public class PCComptabiliserProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = "";
    private String dateComptable = "";
    private String dateEcheancePaiement = "";
    private String emailObject = "";
    private String idLot = null;
    private String idOrganeExecution = "";
    private String numeroOG = "";

    // SEPA iso20002
    private String isoCsTypeAvis = "";
    private String isoGestionnaire = "";
    private String isoHightPriority = "";

    /**
     * Méthode appelé après le finnaly du process en lui même Test si le process n'est pas en erreur, et si c'est ok
     * lance la suite du traitement En l'occurence la mise en GED auto des décisions du lot
     * 
     * @param simpleLot
     *            l'instance du lot qui doit être traité
     * @throws Exception
     *             l'exception succeptible d'être levé
     */
    private void afterProcess(SimpleLot simpleLot) throws Exception {
        // si process pas en erreur
        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {

            // on check si c'est un lot de type decision
            if (simpleLot.getCsTypeLot().equals(IRELot.CS_TYP_LOT_DECISION)) {
                // on vide les logs, afin de logger les infos reltaives au process de mise en ged
                // et de supprimer les logs relatifs à la comptabilisation des décisions
                JadeThread.logClear();
                launchMiseEnGedDecisionApresCalcul(simpleLot);
                sendJobForChysaor(simpleLot.getIdLot());
            }
        }
    }

    private void sendJobForChysaor(String idLot) {
        try {

            if (ChrysaorUtil.isChrysaorEnabled()) {
                PegasusServiceLocator.getChrysaorService().sendJobFor(ExternalJobActionSource.COMPTABILISATION,
                        new ComptabilisationParameter(idLot));
            }
        } catch (Exception e) {
            JadeLogger.info(this, " [Chrysaor] The chrysaor service throw exception during submitting job for idLot:"
                    + idLot + ", message: " + e.getMessage());
        }

    }

    /**
     * Mandatory paramètres appelé depuis le helpers pour permettre un reafficher si erreur
     * 
     * @throws ComptabiliserLotException
     */
    public void checkMandatoryParams(BSession session) throws ComptabiliserLotException {
        if (idLot == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the idLot is null!");
        }

        if (dateComptable == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the dateValeur is null!");
        }

        if (JadeStringUtil.isEmpty(dateEcheancePaiement)) {
            throw new ComptabiliserLotException("La date Echeance est obligatoire !");
        }

        if (JadeStringUtil.isEmpty(idOrganeExecution)) {
            throw new ComptabiliserLotException("L'idOrganeExecution est obligatoire !");
        }
        if (!isIso20022(idOrganeExecution, session)) {
            if (JadeStringUtil.isEmpty(numeroOG)) {
                throw new ComptabiliserLotException("le numero OG est obligatoire !");
            }
        }
    }

    private boolean isIso20022(String idOrganeExecution, BSession session) throws ComptabiliserLotException {
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession(session);
        mgr.setForIdOrganeExecution(idOrganeExecution);
        try {
            mgr.find();
            if (mgr.size() != 1) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ComptabiliserLotException("L'idOrganeExecution est inconnu !");
        }

        return ((CAOrganeExecution) mgr.getEntity(0)).getIdTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022);
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getDateComptable() {
        return dateComptable;
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    @Override
    public String getDescription() {
        return "Comptablisation des lot";
    }

    public String getEmailObject() {
        return emailObject;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    private ArrayList<String> getIdsDecisionToPrintForlot(String idLot) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PrestationException {

        // liste des prestations
        ArrayList<String> listeIdsPrestations = new ArrayList<String>();
        PrestationSearch prestSearch = new PrestationSearch();
        prestSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        prestSearch.setForIdLot(idLot);

        // iteration sur les résultats
        for (JadeAbstractModel model : PegasusServiceLocator.getPrestationService().search(prestSearch)
                .getSearchResults()) {
            listeIdsPrestations.add(((Prestation) model).getId());
        }
        // liste des idDecisions
        ArrayList<String> idsDecisionAc = new ArrayList<String>();
        DecisionApresCalculSearch dacSearch = new DecisionApresCalculSearch();
        dacSearch.setForIdPrestationsIn(listeIdsPrestations);
        dacSearch.setWhereKey(DecisionApresCalculSearch.FOR_MISE_EN_GED_COMPTA_WHERE_KEY);
        dacSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        for (JadeAbstractModel model : PegasusServiceLocator.getDecisionApresCalculService().search(dacSearch)
                .getSearchResults()) {
            idsDecisionAc.add(((DecisionApresCalcul) model).getSimpleDecisionApresCalcul().getIdDecisionApresCalcul());
        }

        return idsDecisionAc;
    }

    private List<String> getMailsDestinataire() {
        List<String> mails = new ArrayList<String>();
        mails.add(adresseMail);
        return mails;
    }

    @Override
    public String getName() {
        return "ComptabiliserLot";
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    /**
     * Point d'entrée de la mise en GED auto des décisions après calcul
     * 
     * @throws Exception
     */
    private void launchMiseEnGedDecisionApresCalcul(SimpleLot simpleLot) throws Exception {

        // récupération de l'utilisateur connecté
        String loggedUser = getSession().getUserId();
        // handler permettant de générer le container de publication
        // DACGedHandler gedHandler = null;// = DACGedHandler.getInstanceForTraitementPourLot(this.idLot,
        // loggedUser,getSession(),this.getIdsDecisionToPrintForlot(this.idLot));

        PCImprimerDecisionsProcess process = new PCImprimerDecisionsProcess();
        process.setSession(getSession());
        process.setIdLot(idLot);
        process.setForGed(Boolean.TRUE);
        process.setIsForLot(Boolean.TRUE);
        process.setDecisionType(DecisionTypes.DECISION_APRES_CALCUL);
        process.setIdDecisionsIdToPrint(getIdsDecisionToPrintForlot(idLot));
        process.setMailGest(adresseMail);
        process.setDateDoc(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // process.setGedHandler(gedHandler);
        BProcessLauncher.startJob(process);
    }

    @Override
    protected void process() throws Exception {

        SimpleLot simpleLot = null;
        try {
            // lancement process de comptabilisation
            ComptabilisationData data = PegasusServiceLocator.getLotService().comptabiliserLot(idLot,
                    idOrganeExecution, numeroOG, null, dateComptable, dateEcheancePaiement);

            BigDecimal totalJournal = new BigDecimal(JANumberFormatter.deQuote(CABusinessServiceLocator
                    .getJournalService().getSommeEcritures(data.getJournalConteneur().getJournalModel())));

            if (totalJournal.signum() == -1) {
                CABusinessServiceLocator.getOrdreGroupeService().createOrdreGroupeeAndPrepare(
                        data.getJournalConteneur().getJournalModel().getId(), idOrganeExecution, numeroOG,
                        dateEcheancePaiement, CAOrdreGroupe.VERSEMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI,
                        isoCsTypeAvis, isoGestionnaire, isoHightPriority);
            }
        } catch (Exception e) {
            this.addError(e);
        } finally {

            simpleLot = CorvusServiceLocator.getLotService().read(idLot);

            sendProcessMail(PROCESS_TYPE.COMPTABILISATION, simpleLot, null);

            // gestion after process, dans le cas des lot de type décision, mis en ged des décisions
            afterProcess(simpleLot);
        }
    }

    /**
     * Envoi du mail suite au process
     * 
     * @param process
     * @param lot
     * @param gedHandler
     * @throws Exception
     */
    private final void sendProcessMail(PROCESS_TYPE process, SimpleLot lot, DACGedHandler gedHandler) throws Exception {
        List<String> mails = new ArrayList<String>();
        mails.add(adresseMail);

        ComptabiliserProcessMailHandler handler = new ComptabiliserProcessMailHandler(process, lot, getSession(),
                getLogSession(), gedHandler);

        handler.sendMail(getMailsDestinataire());
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHightPriority() {
        return isoHightPriority;
    }

    public void setIsoHightPriority(String isoHightPriority) {
        this.isoHightPriority = isoHightPriority;
    }

}
