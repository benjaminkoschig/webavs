package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.contentieux.CADossierEtapeManager;
import globaz.osiris.print.list.CAListDossiersEtape;

/**
 * @author sel Créé le 06 aout 2007
 */
public class CAProcessListDossierEtape extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtape = "";
    private String dateValue = "";
    private String sequence = "";

    public CAProcessListDossierEtape() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CADossierEtapeManager manager = new CADossierEtapeManager();
            manager.setSession(getSession());
            manager.setCsEtape(getCsEtape());
            manager.setIdSequence(getSequence());
            manager.setForTriListeCA("2");
            manager.setForTriListeSection("1");
            manager.find();

            // Création du document
            CAListDossiersEtape excelDoc = new CAListDossiersEtape(getSession());
            excelDoc.setCsEtape(getCsEtape());
            excelDoc.setSequence(getSequence());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
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
     * @return the idEtape
     */
    public String getCsEtape() {
        return csEtape;
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
            return getSession().getLabel("LISTE_DOSSIERS_ETAPE_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("LISTE_DOSSIERS_ETAPE_SUJETMAIL_OK");
        }
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
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
     * @param idEtape
     *            the idEtape to set
     */
    public void setCsEtape(String idEtape) {
        csEtape = idEtape;
    }

    /**
     * @param dateValue
     *            the dateValue to set
     */
    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}
