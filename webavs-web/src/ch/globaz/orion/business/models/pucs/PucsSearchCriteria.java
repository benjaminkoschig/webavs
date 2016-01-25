package ch.globaz.orion.business.models.pucs;

public class PucsSearchCriteria {

    // Codes systèmes status
    public final static String CS_HANDLED = "1000001";
    public final static String CS_HANDLING = "1000002";
    public final static String CS_REJECTED = "1000003";
    public final static String CS_TO_HANDLE = "1000004";
    public final static String CS_UPLOADED = "1000005";
    private String forDecalarationYear = null;
    private String forStatusType = null;
    private String fromDateChangeStatus = null;
    private String likeAffileNumber = null;

    public String getForDecalarationYear() {
        return forDecalarationYear;
    }

    public String getForStatusType() {
        return forStatusType;
    }

    public String getFromDateChangeStatus() {
        return fromDateChangeStatus;
    }

    public String getLikeAffileNumber() {
        return likeAffileNumber;
    }

    public void setForDecalarationYear(String forDecalarationYear) {
        this.forDecalarationYear = forDecalarationYear;
    }

    public void setForStatusType(String forStatusType) {
        this.forStatusType = forStatusType;
    }

    public void setFromDateChangeStatus(String fromDateChangeStatus) {
        this.fromDateChangeStatus = fromDateChangeStatus;
    }

    public void setLikeAffileNumber(String likeAffileNumber) {
        this.likeAffileNumber = likeAffileNumber;
    }

}
