package globaz.aquila.process;

import globaz.aquila.db.access.plaintes.COPlainteForExcelListManager;
import globaz.aquila.print.list.COListPlainteExcel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class COProcessListPlainteExcel extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String eMailAddress;
    public String fromDate;
    public boolean isEmpty = false;
    public String untilDate;
    public String withoutEndDate;

    public COProcessListPlainteExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            COPlainteForExcelListManager manager = new COPlainteForExcelListManager();
            manager.setSession(getSession());
            manager.setPeriodeDu(getFromDate());
            manager.setPeriodeAu(getUntilDate());
            manager.setWithoutEndDate(getWithoutEndDate());
            manager.find();

            if (manager.size() == 0) {
                isEmpty = true;
            }

            COListPlainteExcel excelDoc = new COListPlainteExcel(getSession());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.setFromDate(getFromDate());
            excelDoc.setUntilDate(getUntilDate());
            excelDoc.setWithoutEndDate(getWithoutEndDate());
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            // registerAttachedDocument(excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    protected String getEMailObject() {
        if (isEmpty) {
            return getSession().getLabel("LIST_PLAINTE_VIDE");
        } else if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LIST_PLAINTE_ECHEC");
        } else {
            return getSession().getLabel("LIST_PLAINTE_SUCCES");
        }
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public String getWithoutEndDate() {
        return withoutEndDate;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public void setWithoutEndDate(String withoutEndDate) {
        this.withoutEndDate = withoutEndDate;
    }

}
