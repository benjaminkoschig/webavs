package globaz.aquila.process;

import globaz.aquila.db.access.suiviprocedure.COStatistiqueManager;
import globaz.aquila.print.list.COListStatistiqueExcel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;

public class COProcessListStatistiqueExcel extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String eMailAddress = null;
    public String fromDate = null;
    public String untilDate = null;

    /**
	 *
	 */
    public COProcessListStatistiqueExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            COStatistiqueManager manager = new COStatistiqueManager();
            manager.setSession(getSession());
            manager.setPeriodeDu(getFromDate());
            manager.setPeriodeAu(getUntilDate());
            manager.setForLangue(getSession().getIdLangue());
            manager.find();

            COListStatistiqueExcel excelDoc = new COListStatistiqueExcel(getSession());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.setFromDate(getFromDate());
            excelDoc.setUntilDate(getUntilDate());
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.toString());
            return false;
        }
    }

    /**
     * @return
     */
    public String geteMailAddress() {
        return eMailAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LIST_STAT_ECHEC");
        } else {
            return getSession().getLabel("LIST_STAT_SUCCES");
        }
    }

    /**
     * @return
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
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

    /**
     * @param eMailAddress
     */
    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    /**
     * @param fromDate
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param untilDate
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

}
