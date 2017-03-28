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
            comptabiliseDebloquage.comptabilise(this, Long.valueOf(idLot));
            succes = true;
        } catch (Exception e) {
            BTransaction transaction = getTransaction();
            transaction.addErrors(e.toString());
            ProcessMailUtils.sendMailError(getEMailAddress(), e, this, "");
            setSendCompletionMail(false);

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

}