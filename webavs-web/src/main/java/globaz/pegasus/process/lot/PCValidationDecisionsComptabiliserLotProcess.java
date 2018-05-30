package globaz.pegasus.process.lot;

import globaz.corvus.api.lots.IRELot;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.process.PCAbstractJob;
import globaz.pegasus.process.decision.PCImprimerDecisionsProcess;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler.PROCESS_TYPE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;
import ch.globaz.pegasus.businessimpl.utils.PCGedUtils;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.utils.PCApplicationUtil;

/**
 * Process pour pouvoir lancer la comptabilisation d'un lot déclenché depuis les PC (comptabilisation auto) en mode
 * batch pour éviter des problème d'acces concurrent constaté entre le synchro et le batch
 * 
 * @author cel
 * 
 */
public class PCValidationDecisionsComptabiliserLotProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 7074734681201329010L;

    private String idLot = "";

    private String idOrganeExecution = "";
    private String numeroOG = "";
    private String dateValeur = "";
    private String dateEcheance = "";
    private String mailAdress = "";

    @Override
    public String getDescription() {
        return "Process de ccomptabilisation d'un lot suite à la validation d'une décision";
    }

    @Override
    public String getName() {
        return "ValisationDecisionComptabiliserLot";
    }

    @Override
    protected void process() throws Exception {
        try {
            PegasusServiceLocator.getLotService().comptabiliserLot(idLot, idOrganeExecution, numeroOG, null,
                    dateValeur, dateEcheance);
        } catch (Exception e) {
            this.addError(e);
        } finally {
            // S170224_009 - ajout de l'impression forcée en fin de validation pour mise en ged auto 
            // WEBAVS-5482 - Seul JU et VD souhaitent cette fonctionnalité, le canton VS est exclu sur base de la prop LOI_CANTONALE_PC("canton.loi.pc")
            SimpleLot simpleLot = CorvusServiceLocator.getLotService().read(idLot);
            if ( (!PCApplicationUtil.isCantonVS()) && (PCGedUtils.isDocumentInGed(IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL, getSession())
                    && simpleLot.getCsTypeLot().equals(IRELot.CS_TYP_LOT_DECISION_RESTITUTION))) {
                launchMiseEnGedDecisionApresCalcul(simpleLot);
            }
            sendProcessMail(PROCESS_TYPE.COMPTABILISATION, simpleLot, null);
        }
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
        process.setMailGest(mailAdress);
        process.setDateDoc(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // process.setGedHandler(gedHandler);
        BProcessLauncher.startJob(process);
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

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_BATCH_JOB_QUEUE;
    };

    /**
     * Envoi du mail suite au process
     * 
     * @param process
     * @param lot
     * @param gedHandler
     * @throws Exception
     */
    private final void sendProcessMail(PROCESS_TYPE process, SimpleLot lot, DACGedHandler gedHandler) throws Exception {

        ComptabiliserProcessMailHandler handler = new ComptabiliserProcessMailHandler(process, lot, getSession(),
                getLogSession(), gedHandler);

        handler.sendMail(getMailsDestinataire());
    }

    private List<String> getMailsDestinataire() {
        List<String> mails = new ArrayList<String>();
        mails.add(mailAdress);
        return mails;
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

    public void setDateValeur(String dateComptable) {
        dateValeur = dateComptable;
    }

    public void setDateEcheance(String dateEcheancePaiement) {
        dateEcheance = dateEcheancePaiement;

    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailProcessCompta) {
        mailAdress = mailProcessCompta;
    }
}
