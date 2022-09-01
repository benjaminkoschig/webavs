package ch.globaz.eform.businessimpl.services.sedex.envoi;

public enum SedexType2021Enum {
    TYPE_101("101"),
    TYPE_102("102");
    private String messageType = "2021";
    private String subMessageType;
    private String action = "6";
    private boolean testDeliveryFlag = true;
    private boolean responseExpected = false;
    private boolean businessCaseClosed = true;
    private boolean leadingDocument = false;
    private int order = 1;
    private boolean pensionRecipient = false;
    private String manufacturer = "Globaz SA";
    private String product = "WEBAVS";
    private String productVersion = "1.29";


    SedexType2021Enum(String subMessageType) {
        this.subMessageType = subMessageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSubMessageType() {
        return subMessageType;
    }

    public String getAction() {
        return action;
    }

    public boolean isTestDeliveryFlag() {
        return testDeliveryFlag;
    }

    public boolean isResponseExpected() {
        return responseExpected;
    }

    public boolean isBusinessCaseClosed() {
        return businessCaseClosed;
    }

    public boolean isLeadingDocument() {
        return leadingDocument;
    }

    public int getOrder() {
        return order;
    }

    public boolean isPensionRecipient() {
        return pensionRecipient;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProduct() {
        return product;
    }

    public String getProductVersion() {
        return productVersion;
    }
}
