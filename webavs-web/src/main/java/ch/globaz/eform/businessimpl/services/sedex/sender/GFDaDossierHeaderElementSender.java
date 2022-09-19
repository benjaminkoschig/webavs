package ch.globaz.eform.businessimpl.services.sedex.sender;

public enum GFDaDossierHeaderElementSender implements GFDaDossierElementSender{
    SENDER_ID("senderId"),
    RECIPIENT_ID("recipientId"),
    MESSAGE_ID("messageId"),
    REFERENCE_MESSAGE_ID("referenceMessageId"),
    BUSINESS_PROCESS_ID("businessProcessId"),
    OUR_BUSINESS_REFERENCE_ID("ourBusinessReferenceId"),
    YOUR_BUSINESS_REFERENCE_ID("yourBusinessReferenceId"),
    MESSAGE_TYPE("messageType"),
    SUB_MESSAGE_TYPE("subMessageType"),
    SUBJECT("subject"),
    COMMENT("comment"),
    MESSAGE_DATE("messageDate"),
    ACTION("action"),
    TEST_DELIVERY_FLAG("testDeliveryFlag"),
    RESPONSE_EXPECTED("responseExpected"),
    BUSINESS_CASE_CLOSED("businessCaseClosed");
    private final String name;

    GFDaDossierHeaderElementSender(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
