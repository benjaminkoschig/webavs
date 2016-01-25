package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.print.CAFailliteForExcelListManager;
import globaz.osiris.print.list.CAListFailliteExcel;

public class CAProcessListFailliteExcel extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String eMailAddress;
    public String forIdCategorie;
    public String forSelectionRole;
    public boolean isEmpty = false;

    protected CAProcessListFailliteExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            CAFailliteForExcelListManager manager = new CAFailliteForExcelListManager();
            manager.setSession(getSession());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForIdCategorie(getForIdCategorie());
            manager.find();

            if (manager.size() == 0) {
                isEmpty = true;
            }

            CAListFailliteExcel excelDoc = new CAListFailliteExcel(getSession());
            excelDoc.setForSelectionRole(getForSelectionRole());
            excelDoc.setForIdCategorie(getForIdCategorie());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.populateSheetListe(manager, getTransaction());
            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
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
            return getSession().getLabel("LIST_FAILLITE_VIDE");
        } else if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("ECHEC_IMPRESSION_LIST_FAILLITE");
        } else {
            return getSession().getLabel("SUCCES_IMPRESSION_LIST_FAILLITE");
        }
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
