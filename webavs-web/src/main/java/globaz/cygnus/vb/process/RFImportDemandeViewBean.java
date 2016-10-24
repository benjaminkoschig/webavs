package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public abstract class RFImportDemandeViewBean extends PRAbstractViewBeanSupport {

    private String eMailAddress = "";
    private String fileName = "";
    private String idGestionnaire = "";
    private String pathFile = "";
    private String userProcess = "";

    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getPathFile() {
        return pathFile;
    }

    public String getUserProcess() {
        return userProcess;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public void setUserProcess(String userProcess) {
        this.userProcess = userProcess;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
