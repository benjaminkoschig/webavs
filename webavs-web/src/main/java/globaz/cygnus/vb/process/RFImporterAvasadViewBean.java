package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author jje
 * 
 */
public class RFImporterAvasadViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSurDocument = "";
    private String eMailAddress = "";
    private String idDecision = "";
    private String idGestionnaire = "";
    private String numeroDecision = "";
    private String pathFile = "";
    private String typeValidation = "";
    private String userProcess = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getPathFile() {
        return pathFile;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    public String getUserProcess() {
        return userProcess;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    public void setUserProcess(String userProcess) {
        this.userProcess = userProcess;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
