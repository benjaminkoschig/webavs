package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPSedexContribuable extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action = "";
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String assessmentDate = "";
    private String assessmentType = "";
    private String country = "";
    private String dateOfEntry = "";
    private String dateOfMaritalStatus = "";
    private String firstName = "";
    private String houseNumber = "";
    private String idRetour = "";
    private String idSedexContribuable = "";
    private String initialMessageDate = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private String locality = "";
    private String localPersonId = "";
    private String localPersonIdCategory = "";
    private String maritalStatus = "";
    private String messageId = "";
    private String officialName = "";
    private String ourBusinessReferenceID = "";
    private String personId = "";
    private String personIdCategory = "";
    private String referenceMessageId = "";
    private String remark = "";
    private String reportType = "";
    private String senderId = "";
    private String sex = "";
    private String street = "";
    private String swissZipCode = "";
    private String town = "";
    private String vn = "";
    private String yearMonthDay = "";
    private String yourBusinessReferenceId = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdSedexContribuable(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPSECON";
        } else {
            return "CPSECOB";
        }
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric("IKIRET");
        idSedexContribuable = statement.dbReadNumeric("SEIDCON");
        senderId = statement.dbReadString("SESENID");
        messageId = statement.dbReadString("SEMESID");
        referenceMessageId = statement.dbReadString("SEREFID");
        ourBusinessReferenceID = statement.dbReadString("SEOBUID");
        yourBusinessReferenceId = statement.dbReadString("SEYBUID");
        assessmentDate = statement.dbReadString("SEASSDA");
        assessmentType = statement.dbReadString("SEASSTY");
        reportType = statement.dbReadString("SEREPTY");
        vn = statement.dbReadString("SEVNAVS");
        personIdCategory = statement.dbReadString("SEPEIDC");
        personId = statement.dbReadString("SEPERID");
        officialName = statement.dbReadString("SEOFNAM");
        firstName = statement.dbReadString("SEFINAM");
        sex = statement.dbReadString("SECSEXE");
        yearMonthDay = statement.dbReadString("SEDATEN");
        street = statement.dbReadString("SESTRET");
        houseNumber = statement.dbReadString("SEHOUSN");
        town = statement.dbReadString("SECTOWN");
        swissZipCode = statement.dbReadString("SESWZIP");
        country = statement.dbReadString("SECOUNT");
        maritalStatus = statement.dbReadString("SEMARST");
        dateOfMaritalStatus = statement.dbReadString("SEDATMA");
        dateOfEntry = statement.dbReadString("SEDAENT");
        remark = statement.dbReadString("SEREMAR");
        localPersonId = statement.dbReadString("SELPEID");
        localPersonIdCategory = statement.dbReadString("SELPEI2");
        addressLine1 = statement.dbReadString("SEADLI1");
        addressLine2 = statement.dbReadString("SEADLI2");
        locality = statement.dbReadString("SELOCAL");
        initialMessageDate = statement.dbReadString("SEINDAT");
        action = statement.dbReadNumeric("SEACTIO");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        if (isForRetourOriginale == false) {
            // Ne pas mettre à jour car cette zone a été défini par erreur comme clé primaire... sinon pb d'intégrité
            statement.writeField("SEIDCON",
                    this._dbWriteNumeric(statement.getTransaction(), idSedexContribuable, "idSedexContribuable"));
        }
        statement.writeField("SESENID", this._dbWriteString(statement.getTransaction(), senderId, "senderId"));
        statement.writeField("SEMESID", this._dbWriteString(statement.getTransaction(), messageId, "messageId"));
        statement.writeField("SEREFID",
                this._dbWriteString(statement.getTransaction(), referenceMessageId, "referenceMessageId"));
        statement.writeField("SEOBUID",
                this._dbWriteString(statement.getTransaction(), ourBusinessReferenceID, "ourBusinessReferenceID"));
        statement.writeField("SEYBUID",
                this._dbWriteString(statement.getTransaction(), yourBusinessReferenceId, "yourBusinessReferenceId"));
        statement.writeField("SEASSDA",
                this._dbWriteString(statement.getTransaction(), assessmentDate, "assessmentDate"));
        statement.writeField("SEASSTY",
                this._dbWriteString(statement.getTransaction(), assessmentType, "assessmentType"));
        statement.writeField("SEREPTY", this._dbWriteString(statement.getTransaction(), reportType, "reportType"));
        statement.writeField("SEVNAVS", this._dbWriteString(statement.getTransaction(), vn, "vn"));
        statement.writeField("SEPEIDC",
                this._dbWriteString(statement.getTransaction(), personIdCategory, "personIdCategory"));
        statement.writeField("SEPERID", this._dbWriteString(statement.getTransaction(), personId, "personId"));
        statement.writeField("SEOFNAM", this._dbWriteString(statement.getTransaction(), officialName, "officialName"));
        statement.writeField("SEFINAM", this._dbWriteString(statement.getTransaction(), firstName, "firstName"));
        statement.writeField("SECSEXE", this._dbWriteString(statement.getTransaction(), sex, "sex"));
        statement.writeField("SEDATEN", this._dbWriteString(statement.getTransaction(), yearMonthDay, "yearMonthDay"));
        statement.writeField("SESTRET", this._dbWriteString(statement.getTransaction(), street, "street"));
        statement.writeField("SEHOUSN", this._dbWriteString(statement.getTransaction(), houseNumber, "houseNumber"));
        statement.writeField("SECTOWN", this._dbWriteString(statement.getTransaction(), town, "town"));
        statement.writeField("SESWZIP", this._dbWriteString(statement.getTransaction(), swissZipCode, "swissZipCode"));
        statement.writeField("SECOUNT", this._dbWriteString(statement.getTransaction(), country, "country"));
        statement
                .writeField("SEMARST", this._dbWriteString(statement.getTransaction(), maritalStatus, "maritalStatus"));
        statement.writeField("SEDATMA",
                this._dbWriteString(statement.getTransaction(), dateOfMaritalStatus, "dateOfMaritalStatus"));
        statement.writeField("SEDAENT", this._dbWriteString(statement.getTransaction(), dateOfEntry, "dateOfEntry"));
        statement.writeField("SEREMAR", this._dbWriteString(statement.getTransaction(), remark, "remark"));
        statement
                .writeField("SELPEID", this._dbWriteString(statement.getTransaction(), localPersonId, "localPersonId"));
        statement.writeField("SELPEI2",
                this._dbWriteString(statement.getTransaction(), localPersonIdCategory, "localPersonIdCategory"));
        statement.writeField("SEADLI1", this._dbWriteString(statement.getTransaction(), addressLine1, "addressLine1"));
        statement.writeField("SEADLI2", this._dbWriteString(statement.getTransaction(), addressLine2, "addressLine2"));
        statement.writeField("SELOCAL", this._dbWriteString(statement.getTransaction(), locality, "locality"));
        statement.writeField("SEINDAT",
                this._dbWriteString(statement.getTransaction(), initialMessageDate, "initialMessageDate"));
        statement.writeField("SEACTIO", this._dbWriteNumeric(statement.getTransaction(), action, "action"));
    }

    public String getAction() {
        return action;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getCountry() {
        return country;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public String getDateOfMaritalStatus() {
        return dateOfMaritalStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getIdSedexContribuable() {
        return idSedexContribuable;
    }

    public String getInitialMessageDate() {
        return initialMessageDate;
    }

    public String getLocality() {
        return locality;
    }

    public String getLocalPersonId() {
        return localPersonId;
    }

    public String getLocalPersonIdCategory() {
        return localPersonIdCategory;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getOurBusinessReferenceID() {
        return ourBusinessReferenceID;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonIdCategory() {
        return personIdCategory;
    }

    public String getReferenceMessageId() {
        return referenceMessageId;
    }

    public String getRemark() {
        return remark;
    }

    public String getReportType() {
        return reportType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSex() {
        return sex;
    }

    public String getStreet() {
        return street;
    }

    public String getSwissZipCode() {
        return swissZipCode;
    }

    public String getTown() {
        return town;
    }

    public String getVn() {
        return vn;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public String getYourBusinessReferenceId() {
        return yourBusinessReferenceId;
    }

    @Override
    public boolean hasSpy() {
        if (isForRetourOriginale()) {
            return false;
        } else {
            return super.hasSpy();
        }
    }

    public boolean isForBackup() {
        return isForBackup;
    }

    public boolean isForRetourOriginale() {
        return isForRetourOriginale;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public void setDateOfMaritalStatus(String dateOfMaritalStatus) {
        this.dateOfMaritalStatus = dateOfMaritalStatus;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setForBackup(boolean isForBackup) {
        this.isForBackup = isForBackup;
    }

    public void setForRetourOriginale(boolean isForRetourOriginale) {
        this.isForRetourOriginale = isForRetourOriginale;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setIdSedexContribuable(String idSedexContribuable) {
        this.idSedexContribuable = idSedexContribuable;
    }

    public void setInitialMessageDate(String initialMessageDate) {
        this.initialMessageDate = initialMessageDate;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setLocalPersonId(String localPersonId) {
        this.localPersonId = localPersonId;
    }

    public void setLocalPersonIdCategory(String localPersonIdCategory) {
        this.localPersonIdCategory = localPersonIdCategory;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setOurBusinessReferenceID(String ourBusinessReferenceID) {
        this.ourBusinessReferenceID = ourBusinessReferenceID;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonIdCategory(String personIdCategory) {
        this.personIdCategory = personIdCategory;
    }

    public void setReferenceMessageId(String referenceMessageId) {
        this.referenceMessageId = referenceMessageId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setSwissZipCode(String swissZipCode) {
        this.swissZipCode = swissZipCode;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public void setYearMonthDay(String yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }

    public void setYourBusinessReferenceId(String yourBusinessReferenceId) {
        this.yourBusinessReferenceId = yourBusinessReferenceId;
    }
}
