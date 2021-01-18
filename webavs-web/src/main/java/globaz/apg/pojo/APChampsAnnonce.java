package globaz.apg.pojo;

import globaz.jade.client.util.JadeStringUtil;

public class APChampsAnnonce implements IAPChampsAnnonceTTT {

    private String accountingMonth;
    private String action;
    private String activityBeforeService;
    private String allowanceCareExpenses;
    private Boolean allowanceFarm;
    private String averageDailyIncome;
    private String basicDailyAmount;
    private String breakRules;
    private String businessProcessId;
    private String controlNumber;
    private String csEtat;
    private Boolean dailyIndemnityGuaranteeAI;
    private String deliveryOfficeBranch;
    private String deliveryOfficeOfficeIdentifier;
    private String endOfPeriod;
    private String envelopeMessageId;
    private String eventDate;
    private String idDroit;
    private String idDroitParent;
    private String insurant;
    private String insurantBirthDate;
    private String insurantDomicileCanton;
    private String insurantDomicileCountry;
    private String insurantDomicileNpa;
    private String insurantMaritalStatus;
    private String insurantSexe;
    private String messageDate;
    private String messageId;
    private String messageType;
    private String numberOfChildren;
    private String numberOfDays;
    private String paymentMethod;
    private String recipientId;
    private String referenceNumber;
    private String senderId;
    private String serviceType;
    private String startOfPeriod;
    private String subMessageType;
    private String timeStamp;
    private String totalAPG;
    private Boolean hasComplementCIAB;

    public APChampsAnnonce() {
        accountingMonth = "";
        action = "";
        activityBeforeService = "";
        allowanceCareExpenses = "";
        allowanceFarm = Boolean.FALSE;
        averageDailyIncome = "";
        basicDailyAmount = "";
        breakRules = "";
        businessProcessId = "";
        controlNumber = "";
        csEtat = "";
        dailyIndemnityGuaranteeAI = Boolean.FALSE;
        deliveryOfficeBranch = "";
        deliveryOfficeOfficeIdentifier = "";
        // this.endOfPeriod = "";
        envelopeMessageId = "";
        eventDate = "";
        idDroit = "";
        idDroitParent = "";
        insurant = "";
        insurantBirthDate = "";
        insurantDomicileCanton = "";
        insurantDomicileCountry = "";
        insurantDomicileNpa = "";
        insurantMaritalStatus = "";
        insurantSexe = "";
        messageDate = "";
        messageId = "";
        messageType = "";
        numberOfChildren = "";
        numberOfDays = "";
        paymentMethod = "";
        recipientId = "";
        referenceNumber = "";
        senderId = "";
        serviceType = "";
        // this.startOfPeriod = "";
        subMessageType = "";
        timeStamp = "";
        totalAPG = "";
        hasComplementCIAB = Boolean.FALSE;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getAccountingMonth()
     */
    @Override
    public String getAccountingMonth() {
        return accountingMonth;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getAction()
     */
    @Override
    public String getAction() {
        return action;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getActivityBeforeService()
     */
    @Override
    public String getActivityBeforeService() {
        return activityBeforeService;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getAllowanceCareExpenses()
     */
    @Override
    public String getAllowanceCareExpenses() {
        return allowanceCareExpenses;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getAllowanceFarm()
     */
    @Override
    public Boolean getAllowanceFarm() {
        return allowanceFarm;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getAverageDailyIncome()
     */
    @Override
    public String getAverageDailyIncome() {
        return averageDailyIncome;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getBasicDailyAmount()
     */
    @Override
    public String getBasicDailyAmount() {
        return basicDailyAmount;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getBreakRules()
     */
    @Override
    public String getBreakRules() {
        return breakRules;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getBusinessProcessId()
     */
    @Override
    public String getBusinessProcessId() {
        return businessProcessId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getBusinessProcessIdFormatted()
     */
    @Override
    public String getBusinessProcessIdFormatted() {
        String bpId = getBusinessProcessId();
        String numCaisse = getDeliveryOfficeOfficeIdentifier();
        String numAgence = getDeliveryOfficeBranch();
        return JadeStringUtil.fillWithZeroes(numCaisse, 3) + "." + JadeStringUtil.fillWithZeroes(numAgence, 3) + "."
                + bpId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getControlNumber()
     */
    @Override
    public String getControlNumber() {
        return controlNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getCsEtat()
     */
    @Override
    public String getCsEtat() {
        return csEtat;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getDailyIndemnityGuaranteeAI()
     */
    @Override
    public Boolean getDailyIndemnityGuaranteeAI() {
        return dailyIndemnityGuaranteeAI;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getDeliveryOfficeBranch()
     */
    @Override
    public String getDeliveryOfficeBranch() {
        return deliveryOfficeBranch;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getDeliveryOfficeOfficeIdentifier()
     */
    @Override
    public String getDeliveryOfficeOfficeIdentifier() {
        return deliveryOfficeOfficeIdentifier;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getEndOfPeriod()
     */
    @Override
    public String getEndOfPeriod() {
        return endOfPeriod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getEnvelopeMessageId()
     */
    @Override
    public String getEnvelopeMessageId() {
        return envelopeMessageId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getEventDate()
     */
    @Override
    public String getEventDate() {
        return eventDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getIdDroit()
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getIdDroitParent()
     */
    @Override
    public String getIdDroitParent() {
        return idDroitParent;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurant()
     */
    @Override
    public String getInsurant() {
        return insurant;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantBirthDate()
     */
    @Override
    public String getInsurantBirthDate() {
        return insurantBirthDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantDomicileCanton()
     */
    @Override
    public String getInsurantDomicileCanton() {
        return insurantDomicileCanton;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantDomicileCountry()
     */
    @Override
    public String getInsurantDomicileCountry() {
        return insurantDomicileCountry;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantDomicileNpa()
     */
    @Override
    public String getInsurantDomicileNpa() {
        return insurantDomicileNpa;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantMaritalStatus()
     */
    @Override
    public String getInsurantMaritalStatus() {
        return insurantMaritalStatus;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getInsurantSexe()
     */
    @Override
    public String getInsurantSexe() {
        return insurantSexe;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getMessageDate()
     */
    @Override
    public String getMessageDate() {
        return messageDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getMessageId()
     */
    @Override
    public String getMessageId() {
        return messageId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getMessageType()
     */
    @Override
    public String getMessageType() {
        return messageType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getNumberOfChildren()
     */
    @Override
    public String getNumberOfChildren() {
        return numberOfChildren;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getNumberOfDays()
     */
    @Override
    public String getNumberOfDays() {
        return numberOfDays;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getPaymentMethod()
     */
    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getRecipientId()
     */
    @Override
    public String getRecipientId() {
        return recipientId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getReferenceNumber()
     */
    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getSenderId()
     */
    @Override
    public String getSenderId() {
        return senderId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getServiceType()
     */
    @Override
    public String getServiceType() {
        return serviceType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getStartOfPeriod()
     */
    @Override
    public String getStartOfPeriod() {
        return startOfPeriod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getSubMessageType()
     */
    @Override
    public String getSubMessageType() {
        return subMessageType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getTimeStamp()
     */
    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getTotalAPG()
     */
    @Override
    public String getTotalAPG() {
        return totalAPG;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#getHasComplementCIAB()
     */
    @Override
    public Boolean getHasComplementCIAB() {
        return hasComplementCIAB;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setAccountingMonth(java.lang.String)
     */
    @Override
    public void setAccountingMonth(String accountingMonth) {
        this.accountingMonth = accountingMonth;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setAction(java.lang.String)
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setActivityBeforeService(java.lang.String)
     */
    @Override
    public void setActivityBeforeService(String activityBeforeService) {
        this.activityBeforeService = activityBeforeService;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setAllowanceCareExpenses(java.lang.String)
     */
    @Override
    public void setAllowanceCareExpenses(String allowanceCareExpenses) {
        this.allowanceCareExpenses = allowanceCareExpenses;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setAllowanceFarm(java.lang.Boolean)
     */
    @Override
    public void setAllowanceFarm(Boolean allowanceFarm) {
        this.allowanceFarm = allowanceFarm;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setAverageDailyIncome(java.lang.String)
     */
    @Override
    public void setAverageDailyIncome(String averageDailyIncome) {
        this.averageDailyIncome = averageDailyIncome;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setBasicDailyAmount(java.lang.String)
     */
    @Override
    public void setBasicDailyAmount(String basicDailyAmount) {
        this.basicDailyAmount = basicDailyAmount;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setBreakRules(java.lang.String)
     */
    @Override
    public void setBreakRules(String breakRules) {
        this.breakRules = breakRules;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setBusinessProcessId(java.lang.String)
     */
    @Override
    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setControlNumber(java.lang.String)
     */
    @Override
    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setCsEtat(java.lang.String)
     */
    @Override
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setDailyIndemnityGuaranteeAI(java.lang.Boolean)
     */
    @Override
    public void setDailyIndemnityGuaranteeAI(Boolean dailyIndemnityGuaranteeAI) {
        this.dailyIndemnityGuaranteeAI = dailyIndemnityGuaranteeAI;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setDeliveryOfficeBranch(java.lang.String)
     */
    @Override
    public void setDeliveryOfficeBranch(String deliveryOfficeBranch) {
        this.deliveryOfficeBranch = deliveryOfficeBranch;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setDeliveryOfficeOfficeIdentifier(java.lang.String)
     */
    @Override
    public void setDeliveryOfficeOfficeIdentifier(String deliveryOfficeOfficeIdentifier) {
        this.deliveryOfficeOfficeIdentifier = deliveryOfficeOfficeIdentifier;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setEndOfPeriod(java.lang.String)
     */
    @Override
    public void setEndOfPeriod(String endOfPeriod) {
        this.endOfPeriod = endOfPeriod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setEnvelopeMessageId(java.lang.String)
     */
    @Override
    public void setEnvelopeMessageId(String envelopeMessageId) {
        this.envelopeMessageId = envelopeMessageId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setEventDate(java.lang.String)
     */
    @Override
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setIdDroit(java.lang.String)
     */
    @Override
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setIdDroitParent(java.lang.String)
     */
    @Override
    public void setIdDroitParent(String idDroitParent) {
        this.idDroitParent = idDroitParent;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurant(java.lang.String)
     */
    @Override
    public void setInsurant(String insurant) {
        this.insurant = insurant;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantBirthDate(java.lang.String)
     */
    @Override
    public void setInsurantBirthDate(String insurantBirthDate) {
        this.insurantBirthDate = insurantBirthDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantDomicileCanton(java.lang.String)
     */
    @Override
    public void setInsurantDomicileCanton(String insurantDomicileCanton) {
        this.insurantDomicileCanton = insurantDomicileCanton;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantDomicileCountry(java.lang.String)
     */
    @Override
    public void setInsurantDomicileCountry(String insurantDomicileCountry) {
        this.insurantDomicileCountry = insurantDomicileCountry;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantDomicileNpa(java.lang.String)
     */
    @Override
    public void setInsurantDomicileNpa(String insurantDomicileNpa) {
        this.insurantDomicileNpa = insurantDomicileNpa;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantMaritalStatus(java.lang.String)
     */
    @Override
    public void setInsurantMaritalStatus(String insurantMaritalStatus) {
        this.insurantMaritalStatus = insurantMaritalStatus;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setInsurantSexe(java.lang.String)
     */
    @Override
    public void setInsurantSexe(String insurantSexe) {
        this.insurantSexe = insurantSexe;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setMessageDate(java.lang.String)
     */
    @Override
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setMessageId(java.lang.String)
     */
    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setMessageType(java.lang.String)
     */
    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setNumberOfChildren(java.lang.String)
     */
    @Override
    public void setNumberOfChildren(String numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setNumberOfDays(java.lang.String)
     */
    @Override
    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setPaymentMethod(java.lang.String)
     */
    @Override
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setRecipientId(java.lang.String)
     */
    @Override
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setReferenceNumber(java.lang.String)
     */
    @Override
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setSenderId(java.lang.String)
     */
    @Override
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setServiceType(java.lang.String)
     */
    @Override
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setStartOfPeriod(java.lang.String)
     */
    @Override
    public void setStartOfPeriod(String startOfPeriod) {
        this.startOfPeriod = startOfPeriod;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setSubMessageType(java.lang.String)
     */
    @Override
    public void setSubMessageType(String subMessageType) {
        this.subMessageType = subMessageType;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setTimeStamp(java.lang.String)
     */
    @Override
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setTotalAPG(java.lang.String)
     */
    @Override
    public void setTotalAPG(String totalAPG) {
        this.totalAPG = totalAPG;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.pojo.IAPAnnonce#setHasComplementCIAB(java.lang.String)
     */
    @Override
    public void setHasComplementCIAB(Boolean hasComplementCIAB) {
        this.hasComplementCIAB = hasComplementCIAB;
    }

}
