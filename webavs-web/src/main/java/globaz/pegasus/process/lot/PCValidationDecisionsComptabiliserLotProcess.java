package globaz.pegasus.process.lot;

import globaz.jade.job.common.JadeJobQueueNames;
import globaz.pegasus.process.PCAbstractJob;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler.PROCESS_TYPE;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;

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
            SimpleLot simpleLot = CorvusServiceLocator.getLotService().read(idLot);
            sendProcessMail(PROCESS_TYPE.COMPTABILISATION, simpleLot, null);
        }
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
