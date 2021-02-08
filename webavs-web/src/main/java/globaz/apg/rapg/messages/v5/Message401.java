package globaz.apg.rapg.messages.v5;

import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.BreakRuleType;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.DeliveryOfficeType;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.InsurantDomicileType;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.PaternityLeaveDataType;
import rapg.ch.ech.xmlns.ech_0044._2.PersonIdentificationType;
import rapg.ch.ech.xmlns.ech_0058._4.SendingApplicationType;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Message401 extends Message implements MessageRAPG {

    @Override
    public XMLGregorianCalendar getAccountingMonth() {
        return content.getAccountingMonth();
    }

    @Override
    public String getAction() {
        return header.getAction();
    }

    @Override
    public BigInteger getActivityBeforeService() {
        return content.getActivityBeforeService();
    }

    @Override
    public BigDecimal getAllowanceCareExpenses() {
        return content.getAllowanceCareExpenses();
    }

    @Override
    public BigDecimal getAverageDailyIncome() {
        return content.getAverageDailyIncome();
    }

    @Override
    public BigDecimal getBasicDailyAmount() {
        return content.getBasicDailyAmount();
    }

    @Override
    public BreakRuleType getBreakRules() {
        return content.getBreakRules();
    }

    @Override
    public String getBusinessProcessId() {
        return header.getBusinessProcessId();
    }

    @Override
    public Long getControlNumber() {
        return content.getControlNumber();
    }

    @Override
    public DeliveryOfficeType getDeliveryOffice() {
        return content.getDeliveryOffice();
    }

    @Override
    public XMLGregorianCalendar getEndOfPeriod() {
        return content.getEndOfPeriod();
    }

    @Override
    public XMLGregorianCalendar getEventDate() {
        return header.getEventDate();
    }

    @Override
    public PersonIdentificationType getInsurant() {
        return content.getInsurant();
    }

    @Override
    public InsurantDomicileType getInsurantDomicile() {
        return content.getInsurantDomicile();
    }

    @Override
    public BigInteger getInsurantMaritalStatus() {
        return content.getInsurantMaritalStatus();
    }

    @Override
    public XMLGregorianCalendar getMessageDate() {
        return header.getMessageDate();
    }

    @Override
    public String getMessageId() {
        return header.getMessageId();
    }

    @Override
    public String getMessageType() {
        return header.getMessageType();
    }

    @Override
    public Short getNumberOfChildren() {
        return content.getNumberOfChildren();
    }

    @Override
    public Short getNumberOfDays() {
        return content.getNumberOfDays();
    }

    @Override
    public BigInteger getPaymentMethod() {
        return content.getPaymentMethod();
    }

    @Override
    public String getRecipientId() {
        return header.getRecipientId();
    }

    @Override
    public String getReferenceMessageId() {
        return header.getReferenceMessageId();
    }

    @Override
    public Integer getReferenceNumber() {
        return content.getReferenceNumber();
    }

    @Override
    public String getSenderId() {
        return header.getSenderId();
    }

    @Override
    public SendingApplicationType getSendingApplication() {
        return header.getSendingApplication();
    }

    @Override
    public BigInteger getServiceType() {
        return content.getServiceType();
    }

    @Override
    public XMLGregorianCalendar getStartOfPeriod() {
        return content.getStartOfPeriod();
    }

    @Override
    public String getSubMessageType() {
        return header.getSubMessageType();
    }

    @Override
    public XMLGregorianCalendar getTimeStamp() {
        return content.getTimeStamp();
    }

    @Override
    public BigDecimal getTotalAPG() {
        return content.getTotalAPG();
    }

    @Override
    public boolean isAllowanceFarm() {
        return content.isAllowanceFarm();
    }

    @Override
    public boolean isDailyIndemnityGuaranteeAI() {
        return content.isDailyIndemnityGuaranteeAI();
    }

    @Override
    public boolean isResponseExpected() {
        return header.isResponseExpected();
    }

    @Override
    public boolean isTestDeliveryFlag() {
        return header.isTestDeliveryFlag();
    }

    @Override
    public void setAccountingMonth(XMLGregorianCalendar value) {
        content.setAccountingMonth(value);
    }

    @Override
    public void setAction(String value) {
        header.setAction(value);
    }

    @Override
    public void setActivityBeforeService(BigInteger value) {
        content.setActivityBeforeService(value);
    }

    @Override
    public void setAllowanceCareExpenses(BigDecimal value) {
        content.setAllowanceCareExpenses(value);
    }

    @Override
    public void setAllowanceFarm(boolean value) {
        content.setAllowanceFarm(value);
    }

    @Override
    public void setAverageDailyIncome(BigDecimal value) {
        content.setAverageDailyIncome(value);
    }

    @Override
    public void setBasicDailyAmount(BigDecimal value) {
        content.setBasicDailyAmount(value);
    }

    @Override
    public void setBreakRules(BreakRuleType value) {
        content.setBreakRules(value);
    }

    @Override
    public void setBusinessProcessId(String value) {
        header.setBusinessProcessId(value);
    }

    @Override
    public void setControlNumber(Long value) {
        content.setControlNumber(value);
    }

    @Override
    public void setDailyIndemnityGuaranteeAI(boolean value) {
        content.setDailyIndemnityGuaranteeAI(value);
    }

    @Override
    public void setDeliveryOffice(DeliveryOfficeType value) {
        content.setDeliveryOffice(value);
    }

    @Override
    public void setEndOfPeriod(XMLGregorianCalendar value) {
        content.setEndOfPeriod(value);
    }

    @Override
    public void setEventDate(XMLGregorianCalendar value) {
        header.setEventDate(value);
    }

    @Override
    public void setInsurant(PersonIdentificationType value) {
        content.setInsurant(value);
    }

    @Override
    public void setInsurantDomicile(InsurantDomicileType value) {
        content.setInsurantDomicile(value);
    }

    @Override
    public void setInsurantMaritalStatus(BigInteger value) {
        content.setInsurantMaritalStatus(value);
    }

    @Override
    public void setMessageDate(XMLGregorianCalendar value) {
        header.setMessageDate(value);
    }

    @Override
    public void setMessageId(String value) {
        header.setMessageId(value);
    }

    @Override
    public void setMessageType(String value) {
        header.setMessageType(value);
    }

    @Override
    public void setNumberOfChildren(Short value) {
        content.setNumberOfChildren(value);
    }

    @Override
    public void setNumberOfDays(Short value) {
        content.setNumberOfDays(value);
    }

    @Override
    public void setPaymentMethod(BigInteger value) {
        content.setPaymentMethod(value);
    }

    @Override
    public void setRecipientId(String value) {
        header.setRecipientId(value);
    }

    @Override
    public void setReferenceMessageId(String value) {
        header.setReferenceMessageId(value);
    }

    @Override
    public void setReferenceNumber(Integer value) {
        content.setReferenceNumber(value);
    }

    @Override
    public void setResponseExpected(boolean value) {
        header.setResponseExpected(value);
    }

    @Override
    public void setSenderId(String value) {
        header.setSenderId(value);
    }

    @Override
    public void setSendingApplication(SendingApplicationType value) {
        header.setSendingApplication(value);
    }

    @Override
    public void setServiceType(BigInteger value) {
        content.setServiceType(value);
    }

    @Override
    public void setStartOfPeriod(XMLGregorianCalendar value) {
        content.setStartOfPeriod(value);
    }

    @Override
    public void setSubMessageType(String value) {
        header.setSubMessageType(value);
    }

    @Override
    public void setTestDeliveryFlag(boolean value) {
        header.setTestDeliveryFlag(value);
    }

    @Override
    public void setTimeStamp(XMLGregorianCalendar value) {
        content.setTimeStamp(value);
    }

    @Override
    public void setTotalAPG(BigDecimal value) {
        content.setTotalAPG(value);
    }

    @Override
    public void setPaternityLeaveData(PaternityLeaveDataType paternityLeaveDataType) {
        content.setPaternityLeaveData(paternityLeaveDataType);
    }

    @Override
    public PaternityLeaveDataType getPaternityLeaveData() {
        return content.getPaternityLeaveData();
    }

}
