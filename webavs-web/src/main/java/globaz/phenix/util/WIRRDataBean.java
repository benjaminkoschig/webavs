package globaz.phenix.util;


public class WIRRDataBean {

    private String nss = "";
    private boolean hasRenteWIRRFounded = false;
    private boolean hasTechnicalError = false;
    private String errorReason = "";
    private String errorDetailedReason = "";
    private String errorComment = "";
    private String messageForUser = "";

    public String getMessageForUser() {
        return messageForUser;
    }

    public void setMessageForUser(String messageForUser) {
        this.messageForUser = messageForUser;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getErrorDetailedReason() {
        return errorDetailedReason;
    }

    public void setErrorDetailedReason(String errorDetailedReason) {
        this.errorDetailedReason = errorDetailedReason;
    }

    public String getErrorComment() {
        return errorComment;
    }

    public void setErrorComment(String errorComment) {
        this.errorComment = errorComment;
    }

    public boolean hasRenteWIRRFounded() {
        return hasRenteWIRRFounded;
    }

    public void setHasRenteWIRRFounded(boolean hasRenteWIRRFounded) {
        this.hasRenteWIRRFounded = hasRenteWIRRFounded;
    }

    public boolean hasTechnicalError() {
        return hasTechnicalError;
    }

    public void setHasTechnicalError(boolean hasTechnicalError) {
        this.hasTechnicalError = hasTechnicalError;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

}