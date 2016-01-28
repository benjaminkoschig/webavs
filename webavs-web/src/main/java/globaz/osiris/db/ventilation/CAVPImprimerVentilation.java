/*
 * Créé le 8 déc. 05
 */
package globaz.osiris.db.ventilation;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmc
 */
public class CAVPImprimerVentilation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCompteAnnexe = "";
    private List forIdSections = new ArrayList();
    private String typeDeProcedure = "";
    private Boolean ventilationGlobale = new Boolean(false);

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
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
        CAVPVentilateur ventilateur = new CAVPVentilateur();
        ventilateur.setSession(getSession());
        ventilateur.setTypeDeProcedure(getTypeDeProcedure());
        ventilateur.initTableauFinal(getForCompteAnnexe(), getForIdSections());
        ventilateur.ventiler(getVentilationGlobale().booleanValue());
        CAVPDocumentImpression excelDoc = new CAVPDocumentImpression(getSession().getLabel("VENTILATION_TITRE"),
                getSession(), ventilateur);
        excelDoc.setIdCompteAnnexe(getForCompteAnnexe());
        excelDoc.setVentilationGlobale(getVentilationGlobale());
        excelDoc.setDocumentInfo(createDocumentInfo());
        excelDoc.populateSheet(getSession());

        // JadePublishDocument publishDoc = new
        // JadePublishDocument(excelDoc.getOutputFile(), createDocumentInfo());
        // registerAttachedDocument(publishDoc);

        this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());

        return !isAborted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("VENTILATION_SUCCESS");
    }

    /**
     * @return
     */
    public String getForCompteAnnexe() {
        return forCompteAnnexe;
    }

    /**
     * @return
     */
    public List getForIdSections() {
        return forIdSections;
    }

    /**
     * @return
     */
    public String getTypeDeProcedure() {
        return typeDeProcedure;
    }

    /**
     * @return the ventilationGlobale
     */
    public Boolean getVentilationGlobale() {
        return ventilationGlobale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    public void setForCompteAnnexe(String string) {
        forCompteAnnexe = string;
    }

    /**
     * @param forIdSections
     */
    public void setForIdSections(List forIdSections) {
        this.forIdSections = forIdSections;
    }

    /**
     * @param string
     */
    public void setTypeDeProcedure(String string) {
        typeDeProcedure = string;
    }

    /**
     * @param ventilationGlobale
     *            the ventilationGlobale to set
     */
    public void setVentilationGlobale(Boolean ventilationGlobale) {
        this.ventilationGlobale = ventilationGlobale;
    }

}
