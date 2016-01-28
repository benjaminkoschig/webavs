package globaz.aquila.process;

import globaz.aquila.db.access.paiement.COPaiementManager;
import globaz.aquila.print.list.COListPaiementExcel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

/**
 * @author USREXT02
 */
public class COProcessListPaiementExcel extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String eMailAddress;
    public String forIdTypeOperation;
    public String forSelectionEtapes;
    public String fromDate;
    public String untilDate;

    public COProcessListPaiementExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            COPaiementManager manager = new COPaiementManager();
            manager.setSession(getSession());
            manager.setFromDate(getFromDate());
            manager.setUntilDate(getUntilDate());
            manager.setForIdTypeOperation(getForIdTypeOperation());
            manager.setForSelectionEtapes(getForSelectionEtapes());
            manager.find();

            // Création du document
            COListPaiementExcel excelDoc = new COListPaiementExcel(getSession());
            excelDoc.setDocumentInfo(createDocumentInfo());
            // excelDoc.setCsSequence(getCsSequence());
            // excelDoc.setCsEtape(getCsEtape());
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            // registerAttachedDocument(excelDoc.getOutputFile());
            return true;

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            e.getMessage();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("AQUILA_PAIEMENT_LISTE_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("AQUILA_PAIEMENT_LISTE_SUJETMAIL_OK");
        }
    }

    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    public String getForSelectionEtapes() {
        return forSelectionEtapes;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getUntilDate() {
        return untilDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdTypeOperation(String forIdTypeOperation) {
        this.forIdTypeOperation = forIdTypeOperation;
    }

    public void setForSelectionEtapes(String forSelectionEtapes) {
        this.forSelectionEtapes = forSelectionEtapes;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

}
