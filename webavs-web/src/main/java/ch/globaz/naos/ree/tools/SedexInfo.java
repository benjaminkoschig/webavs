package ch.globaz.naos.ree.tools;

public class SedexInfo {

    private String sedexSenderId;
    private boolean testDeliveryFlag = false;

    /**
     * @param sedexSenderId
     * @param sedexRecipientId
     * @param sedexAdapteurIP
     * @param sedexOutboxFolder
     */
    public SedexInfo(String sedexSenderId, boolean testDeliveryFlag) {
        this.sedexSenderId = sedexSenderId;
        this.testDeliveryFlag = testDeliveryFlag;

    }

    public String getSedexSenderId() {
        return sedexSenderId;
    }

    public boolean getTestDeliveryFlag() {
        return testDeliveryFlag;
    }

    // public String getSedexOutboxFolder() {
    // return sedexOutboxFolder;
    // }
    //
    // public String getSedexAdapteurIP() {
    // return sedexAdapteurIP;
    // }
    //
    // public String getSedexRecipientId() {
    // return sedexRecipientId;
    // }
}
