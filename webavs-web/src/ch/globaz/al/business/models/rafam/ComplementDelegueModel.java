package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ComplementDelegueModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String allowanceAmount = null;
    private String beneficiaryEndDate = null;
    private String beneficiaryStartDate = null;
    private String businessId = null;
    private String eventDate = null;
    private String idComplement = null;
    private String messageCompanyName = null;
    private String messageDate = null;
    private String messageFakId = null;
    private String messageFakName = null;
    private String messageId = null;
    private String messageMailResponsiblePerson = null;
    private String messageNameResponsiblePerson = null;
    private String messageTelResponsiblePerson = null;
    private String recordNumber = null;

    public String getAllowanceAmount() {
        return allowanceAmount;
    }

    public String getBeneficiaryEndDate() {
        return beneficiaryEndDate;
    }

    public String getBeneficiaryStartDate() {
        return beneficiaryStartDate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getEventDate() {
        return eventDate;
    }

    @Override
    public String getId() {
        return idComplement;
    }

    public String getIdComplement() {
        return idComplement;
    }

    public String getMessageCompanyName() {
        return messageCompanyName;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getMessageFakId() {
        return messageFakId;
    }

    public String getMessageFakName() {
        return messageFakName;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageMailResponsiblePerson() {
        return messageMailResponsiblePerson;
    }

    public String getMessageNameResponsiblePerson() {
        return messageNameResponsiblePerson;
    }

    public String getMessageTelResponsiblePerson() {
        return messageTelResponsiblePerson;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setAllowanceAmount(String allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public void setBeneficiaryEndDate(String beneficiaryEndDate) {
        this.beneficiaryEndDate = beneficiaryEndDate;
    }

    public void setBeneficiaryStartDate(String beneficiaryStartDate) {
        this.beneficiaryStartDate = beneficiaryStartDate;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public void setId(String id) {
        idComplement = id;

    }

    public void setIdComplement(String idComplement) {
        this.idComplement = idComplement;
    }

    public void setMessageCompanyName(String messageCompanyName) {
        this.messageCompanyName = messageCompanyName;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public void setMessageFakId(String messageFakId) {
        this.messageFakId = messageFakId;
    }

    public void setMessageFakName(String messageFakName) {
        this.messageFakName = messageFakName;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageMailResponsiblePerson(String messageMailResponsiblePerson) {
        this.messageMailResponsiblePerson = messageMailResponsiblePerson;
    }

    public void setMessageNameResponsiblePerson(String messageNameResponsiblePerson) {
        this.messageNameResponsiblePerson = messageNameResponsiblePerson;
    }

    public void setMessageTelResponsiblePerson(String messageTelResponsiblePerson) {
        this.messageTelResponsiblePerson = messageTelResponsiblePerson;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

}
