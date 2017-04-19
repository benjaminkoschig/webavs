package globaz.corvus.process;

import globaz.corvus.module.compta.deblocage.REComptabiliseDebloquage;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import ch.globaz.common.process.ProcessMailUtils;

public class REDebloquerRenteComptablisationProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String emailObject;
    private String idLot;
    private String numeroOG;
    private String idOrganeExecution;
    private String dateEcheancePaiement;
    private String dateValeurComptable;
    private String isoGestionnaire;
    private String isoHighPriority;

    public REDebloquerRenteComptablisationProcess() {
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean succes = false;

        try {
            doValidation();
            REComptabiliseDebloquage comptabiliseDebloquage = new REComptabiliseDebloquage(getSession());
            comptabiliseDebloquage.comptabilise(this, Long.valueOf(idLot), numeroOG, idOrganeExecution,
                    dateEcheancePaiement, dateValeurComptable, isoGestionnaire, isoHighPriority);
            succes = true;
        } catch (Exception e) {
            BTransaction transaction = getTransaction();
            transaction.addErrors(e.toString());
            ProcessMailUtils.sendMailError(getEMailAddress(), e, this, "");
            setSendCompletionMail(false);
            transaction.rollback();
            getSession().getCurrentThreadTransaction().rollback();
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
        } finally {
            if (succes) {
                emailObject = getSession().getLabel("EMAIL_OBJECT_DEBLOQUER_MNT_RA_SUCCESS");
            } else {
                emailObject = getSession().getLabel("EMAIL_OBJECT_DEBLOQUER_MNT_RA_ERREUR");
            }
        }

        return succes;
    }

    private void doValidation() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }
            setControleTransaction(getTransaction() == null);
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public String getDateValeurComptable() {
        return dateValeurComptable;
    }

    public void setDateValeurComptable(String dateValeurComptable) {
        this.dateValeurComptable = dateValeurComptable;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public void setIsoHighPriority(String isoHighPriority) {
        this.isoHighPriority = isoHighPriority;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }
}
