package globaz.corvus.process;

import globaz.corvus.itext.REListesAvances;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;

public class REListesAvancesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Format : mm.aaaa
    private String datePaiement = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = getTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }

        boolean succes = true;
        try {

            REListesAvances listAvances = new REListesAvances();
            listAvances.setParentWithCopy(this);
            listAvances.setTransaction(getTransaction());
            listAvances.setControleTransaction(true);
            listAvances.setDatePaiement(getDatePaiement());
            listAvances.executeProcess();

            JadePublishDocumentInfo info = createDocumentInfo();
            info.setPublishDocument(true);
            info.setArchiveDocument(false);

            this.mergePDF(info, true, 500, false, null);

            return true;

        } catch (Exception e) {
            succes = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    protected void _validate() throws Exception {
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

        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LST_AVANCES_EMAIL_OK");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

}
