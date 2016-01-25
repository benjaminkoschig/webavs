package globaz.apg.rapg.messages;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.eahv_iv.xmlns.eahv_iv_2015_common._4.BreakRuleType;
import ch.eahv_iv.xmlns.eahv_iv_2015_common._4.DeliveryOfficeType;
import ch.eahv_iv.xmlns.eahv_iv_2015_common._4.InsurantDomicileType;
import ch.ech.xmlns.ech_0044._2.PersonIdentificationType;
import ch.ech.xmlns.ech_0058._4.SendingApplicationType;

public interface MessageRAPG {

    /**
     * Gets the value of the accountingMonth property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getAccountingMonth();

    /**
     * Gets the value of the action property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAction();

    /**
     * Gets the value of the activityBeforeService property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getActivityBeforeService();

    /**
     * Gets the value of the allowanceCareExpenses property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAllowanceCareExpenses();

    /**
     * Gets the value of the averageDailyIncome property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAverageDailyIncome();

    /**
     * Gets the value of the basicDailyAmount property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getBasicDailyAmount();

    /**
     * Gets the value of the breakRules property.
     * 
     * @return possible object is {@link BreakRuleType }
     * 
     */
    public BreakRuleType getBreakRules();

    /**
     * Gets the value of the businessProcessId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBusinessProcessId();

    /**
     * Gets the value of the controlNumber property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getControlNumber();

    /**
     * Gets the value of the deliveryOffice property.
     * 
     * @return possible object is {@link DeliveryOfficeType }
     * 
     */
    public DeliveryOfficeType getDeliveryOffice();

    /**
     * Gets the value of the endOfPeriod property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getEndOfPeriod();

    /**
     * Gets the value of the eventDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getEventDate();

    /**
     * Gets the value of the insurant property.
     * 
     * @return possible object is {@link PersonIdentificationType }
     * 
     */
    public PersonIdentificationType getInsurant();

    /**
     * Gets the value of the insurantDomicile property.
     * 
     * @return possible object is {@link InsurantDomicileType }
     * 
     */
    public InsurantDomicileType getInsurantDomicile();

    /**
     * Gets the value of the insurantMaritalStatus property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInsurantMaritalStatus();

    /**
     * Retourne l'objet JAXB
     * 
     * @return Objet hérité
     */
    // public Object getJaxbObject();

    /**
     * Gets the value of the messageDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getMessageDate();

    /**
     * Gets the value of the messageId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMessageId();

    /**
     * Gets the value of the messageType property.
     * 
     */
    public String getMessageType();

    /**
     * Gets the value of the minorVersion property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getMinorVersion();

    /**
     * Gets the value of the numberOfChildren property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getNumberOfChildren();

    /**
     * Gets the value of the numberOfDays property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getNumberOfDays();

    /**
     * Gets the value of the paymentMethod property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getPaymentMethod();

    /**
     * Gets the value of the recipientId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRecipientId();

    /**
     * Gets the value of the referenceMessageId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getReferenceMessageId();

    /**
     * Gets the value of the referenceNumber property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getReferenceNumber();

    /**
     * Gets the value of the senderId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSenderId();

    /**
     * Gets the value of the sendingApplication property.
     * 
     * @return possible object is {@link SendingApplicationType }
     * 
     */
    public SendingApplicationType getSendingApplication();

    /**
     * Gets the value of the serviceType property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getServiceType();

    /**
     * Gets the value of the startOfPeriod property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getStartOfPeriod();

    /**
     * Gets the value of the subMessageType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSubMessageType();

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getTimeStamp();

    /**
     * Gets the value of the totalAPG property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getTotalAPG();

    /**
     * Gets the value of the allowanceFarm property.
     * 
     * @return possible object is {@link boolean }
     * 
     */
    public boolean isAllowanceFarm();

    /**
     * Gets the value of the dailyIndemnityGuaranteeAI property.
     * 
     * @return possible object is {@link boolean }
     * 
     */
    public boolean isDailyIndemnityGuaranteeAI();

    /**
     * Gets the value of the responseExpected property.
     * 
     */
    public boolean isResponseExpected();

    /**
     * Gets the value of the testDeliveryFlag property.
     * 
     */
    public boolean isTestDeliveryFlag();

    /**
     * Sets the value of the accountingMonth property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setAccountingMonth(XMLGregorianCalendar value);

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAction(String value);

    /**
     * Sets the value of the activityBeforeService property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setActivityBeforeService(BigInteger value);

    /**
     * Sets the value of the allowanceCareExpenses property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAllowanceCareExpenses(BigDecimal value);

    /**
     * Sets the value of the allowanceFarm property.
     * 
     * @param value
     *            allowed object is {@link boolean }
     * 
     */
    public void setAllowanceFarm(boolean value);

    /**
     * Sets the value of the averageDailyIncome property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAverageDailyIncome(BigDecimal value);

    /**
     * Sets the value of the basicDailyAmount property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setBasicDailyAmount(BigDecimal value);

    /**
     * Sets the value of the breakRules property.
     * 
     * @param value
     *            allowed object is {@link BreakRuleType }
     * 
     */
    public void setBreakRules(BreakRuleType value);

    /**
     * Sets the value of the businessProcessId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBusinessProcessId(String value);

    /**
     * Sets the value of the controlNumber property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setControlNumber(Long value);

    /**
     * Sets the value of the dailyIndemnityGuaranteeAI property.
     * 
     * @param value
     *            allowed object is {@link boolean }
     * 
     */
    public void setDailyIndemnityGuaranteeAI(boolean value);

    /**
     * Sets the value of the deliveryOffice property.
     * 
     * @param value
     *            allowed object is {@link DeliveryOfficeType }
     * 
     */
    public void setDeliveryOffice(DeliveryOfficeType value);

    /**
     * Sets the value of the endOfPeriod property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setEndOfPeriod(XMLGregorianCalendar value);

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setEventDate(XMLGregorianCalendar value);

    /**
     * Sets the value of the insurant property.
     * 
     * @param value
     *            allowed object is {@link PersonIdentificationType }
     * 
     */
    public void setInsurant(PersonIdentificationType value);

    /**
     * Sets the value of the insurantDomicile property.
     * 
     * @param value
     *            allowed object is {@link InsurantDomicileType }
     * 
     */
    public void setInsurantDomicile(InsurantDomicileType value);

    /**
     * Sets the value of the insurantMaritalStatus property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInsurantMaritalStatus(BigInteger value);

    /**
     * Sets the value of the messageDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setMessageDate(XMLGregorianCalendar value);

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMessageId(String value);

    /**
     * Sets the value of the messageType property.
     * 
     */
    public void setMessageType(String value);

    /**
     * Sets the value of the minorVersion property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setMinorVersion(BigInteger value);

    /**
     * Sets the value of the numberOfChildren property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setNumberOfChildren(BigInteger value);

    /**
     * Sets the value of the numberOfDays property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setNumberOfDays(BigInteger value);

    /**
     * Sets the value of the paymentMethod property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setPaymentMethod(BigInteger value);

    /**
     * Sets the value of the recipientId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRecipientId(String value);

    /**
     * Sets the value of the referenceMessageId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setReferenceMessageId(String value);

    /**
     * Sets the value of the referenceNumber property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setReferenceNumber(BigInteger value);

    /**
     * Sets the value of the responseExpected property.
     * 
     */
    public void setResponseExpected(boolean value);

    /**
     * Sets the value of the senderId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSenderId(String value);

    /**
     * Sets the value of the sendingApplication property.
     * 
     * @param value
     *            allowed object is {@link SendingApplicationType }
     * 
     */
    public void setSendingApplication(SendingApplicationType value);

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setServiceType(BigInteger value);

    /**
     * Sets the value of the startOfPeriod property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setStartOfPeriod(XMLGregorianCalendar value);

    /**
     * Sets the value of the subMessageType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSubMessageType(String value);

    /**
     * Sets the value of the testDeliveryFlag property.
     * 
     */
    public void setTestDeliveryFlag(boolean value);

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setTimeStamp(XMLGregorianCalendar value);

    /**
     * Sets the value of the totalAPG property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setTotalAPG(BigDecimal value);

}
