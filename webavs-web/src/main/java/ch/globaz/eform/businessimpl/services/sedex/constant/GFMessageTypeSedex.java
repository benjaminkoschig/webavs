package ch.globaz.eform.businessimpl.services.sedex.constant;

public enum GFMessageTypeSedex {
    TYPE_2021_DEMANDE("2021", "000101"),
    TYPE_2021_TRANSFERE("2021", "000102");
    private String messageType;
    private String subMessageType;
    private boolean testDeliveryFlag = true;
    private boolean responseExpected = false;
    private boolean businessCaseClosed = true;
    private boolean leadingDocument = false;
    private boolean pensionRecipient = false;

    GFMessageTypeSedex(String messageType, String subMessageType) {
        this.messageType = messageType;
        this.subMessageType = subMessageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSubMessageType() {
        return subMessageType;
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

    public boolean isPensionRecipient() {
        return pensionRecipient;
    }
}
