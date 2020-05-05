package globaz.apg.util;

import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;
import ch.eahv.seodor.eahv000101._1.AddressInformationType;
import ch.eahv.seodor.eahv000101._1.InsurantDomicileType;
import ch.eahv.seodor.eahv000101._1.InsurantType;

import javax.xml.datatype.XMLGregorianCalendar;

public class APGSeodorDataBean {

    private String nss;
    private XMLGregorianCalendar startDate;
    private GetServicePeriodsRequestType.Message message;

    private AddressInformationType addressInformation;
    private String annotation;
    private int controlNumber;
    private String departmentId;
    private String emailAddress;
    private InsurantDomicileType insurantDomicileType;
    private InsurantType insurantType;
    private String locator;
    private String mobilePhone;
    private Long numberOfDays;
    private String personalNumber;
    private String referenceNumber;
    private XMLGregorianCalendar serviceEntryDate;
    private int serviceType;
    private XMLGregorianCalendar startOfPeriod;
    private String userId;

    private boolean hasTechnicalError = false;

    public APGSeodorDataBean(){
    }

    public APGSeodorDataBean(APGSeodorDataBean newApgSeodorDataBean){
        this.nss = newApgSeodorDataBean.getNss();
        this.startDate = newApgSeodorDataBean.getStartDate();
        this.message = newApgSeodorDataBean.getMessage();
    }

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public String getNss() {
        return nss;
    }

    public GetServicePeriodsRequestType.Message getMessage() {
        return message;
    }

    public boolean isHasTechnicalError(){
        return hasTechnicalError;
    }

    public void setHasTechnicalError(boolean hasTechnicalError) {
        this.hasTechnicalError = hasTechnicalError;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public void setMessage(GetServicePeriodsRequestType.Message message) {
        this.message = message;
    }

    public AddressInformationType getAddressInformation() {
        return addressInformation;
    }

    public void setAddressInformation(AddressInformationType addressInformation) {
        this.addressInformation = addressInformation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public int getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(int controlNumber) {
        this.controlNumber = controlNumber;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public InsurantDomicileType getInsurantDomicileType() {
        return insurantDomicileType;
    }

    public void setInsurantDomicileType(InsurantDomicileType insurantDomicileType) {
        this.insurantDomicileType = insurantDomicileType;
    }

    public InsurantType getInsurantType() {
        return insurantType;
    }

    public void setInsurantType(InsurantType insurantType) {
        this.insurantType = insurantType;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public XMLGregorianCalendar getServiceEntryDate() {
        return serviceEntryDate;
    }

    public void setServiceEntryDate(XMLGregorianCalendar serviceEntryDate) {
        this.serviceEntryDate = serviceEntryDate;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public XMLGregorianCalendar getStartOfPeriod() {
        return startOfPeriod;
    }

    public void setStartOfPeriod(XMLGregorianCalendar startOfPeriod) {
        this.startOfPeriod = startOfPeriod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
