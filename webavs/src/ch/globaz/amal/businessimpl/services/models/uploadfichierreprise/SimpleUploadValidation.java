package ch.globaz.amal.businessimpl.services.models.uploadfichierreprise;

public class SimpleUploadValidation {
    private String errorMsg = "";
    private int returnCode = 0;

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

}
