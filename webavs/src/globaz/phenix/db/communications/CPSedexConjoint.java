package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;

public class CPSedexConjoint extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String country = "";
    private String dateOfEntry = "";
    private String dateOfMaritalStatus = "";
    private String firstName = "";
    private String houseNumber = "";
    private String idRetour = "";
    private String idSedexConjoint = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private String maritalStatus = "";
    private String officialName = "";
    private String personId = "";
    private String personIdCategory = "";
    private String sex = "";
    private String street = "";
    private String swissZipCode = "";
    private String town = "";
    private String vn = "";
    private String yearMonthDay = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {

        setIdSedexConjoint(this._incCounter(transaction, "0"));

    }

    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPSEFEM";
        } else {
            return "CPSEFEB";
        }
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric("IKIRET");
        idSedexConjoint = statement.dbReadNumeric("SEIDFEM");
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

    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));

    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        if (isForRetourOriginale == false) {
            // Ne pas mettre à jour car cette zone a été défini par erreur comme clé primaire... sinon pb d'intégrité
            statement.writeField("SEIDFEM",
                    this._dbWriteNumeric(statement.getTransaction(), idSedexConjoint, "idSedexConjoint"));
        }
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

    public String getIdSedexConjoint() {
        return idSedexConjoint;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonIdCategory() {
        return personIdCategory;
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

    public void setIdSedexConjoint(String idSedexConjoint) {
        this.idSedexConjoint = idSedexConjoint;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonIdCategory(String personIdCategory) {
        this.personIdCategory = personIdCategory;
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

}
