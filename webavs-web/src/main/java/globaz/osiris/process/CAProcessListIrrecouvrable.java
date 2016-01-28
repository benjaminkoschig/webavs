package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.contentieux.CAIrrecouvrableManager;
import globaz.osiris.print.list.parser.CAListIrrecouvrableParser;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAProcessListIrrecouvrable extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValue = null;

    public CAProcessListIrrecouvrable() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CAIrrecouvrableManager manager = new CAIrrecouvrableManager();
            manager.setSession(getSession());
            manager.setDateValue((new JADate(getDateValue()).toAMJ()).toString());
            manager.find();

            // Création du document
            CAListIrrecouvrableParser excelDoc = new CAListIrrecouvrableParser(getSession());
            excelDoc.setDocumentInfo(createDocumentInfo());
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                excelDoc.populateSheetListe(manager, getTransaction());
            } else {
                excelDoc.populateSheetListeAquila(manager, getTransaction());
            }

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;

        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * @return the dateValue
     */
    public String getDateValue() {
        return dateValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("LISTE_IRR_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("LISTE_IRR_SUJETMAIL_OK");
        }
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
     * @param dateValue
     *            the dateValue to set
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

}
