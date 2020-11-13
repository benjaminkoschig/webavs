package globaz.apg.db.importation.beneficiaires;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

public class APBeneficiary {

    private String numAVS;
    private String name;
    private String firstName;
    private Double amountLastAnnualIncome;
    private Double amountLastIncome;
    private BigInteger yearLastAnnualIncome;
    private String unitLastAnnualIncome;
    private XMLGregorianCalendar dateOfBirth;
    private String nameStreetWithNumber;
    private String zipCode;
    private String town;
    private String phoneNumber;
    private String email;
    private String positionType;
    private boolean reduceWorkHours;
    private boolean rhtIndemnity;
    private boolean assimilablePosition;
    private boolean hasLossOfProfit;

    public APBeneficiary(){
        numAVS = "";
        name = "";
        firstName = "";
        nameStreetWithNumber = "";
        zipCode = "";
        town = "";
        phoneNumber = "";
        email = "";
        amountLastAnnualIncome = 0.d;
        amountLastIncome = 0.d;
        yearLastAnnualIncome = new BigInteger("0");
        unitLastAnnualIncome = "CHF";
        positionType = "";
        reduceWorkHours = false;
        rhtIndemnity = false;
        assimilablePosition = false;
        hasLossOfProfit = false;
    }

    public String getNumAVS() {
        return numAVS;
    }

    public void setNumAVS(String numAVS) {
        this.numAVS = numAVS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(XMLGregorianCalendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNameStreetWithNumber() {
        return nameStreetWithNumber;
    }

    public void setNameStreetWithNumber(String nameStreetWithNumber) {
        this.nameStreetWithNumber = nameStreetWithNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAmountLastAnnualIncome() {
        return amountLastAnnualIncome;
    }

    public void setAmountLastAnnualIncome(Double amountLastAnnualIncome) {
        this.amountLastAnnualIncome = amountLastAnnualIncome;
    }

    public BigInteger getYearLastAnnualIncome() {
        return yearLastAnnualIncome;
    }

    public void setYearLastAnnualIncome(BigInteger yearLastAnnualIncome) {
        this.yearLastAnnualIncome = yearLastAnnualIncome;
    }

    public String getUnitLastAnnualIncome() {
        return unitLastAnnualIncome;
    }

    public void setUnitLastAnnualIncome(String unitLastAnnualIncome) {
        this.unitLastAnnualIncome = unitLastAnnualIncome;
    }

    public Double getAmountLastIncome() {
        return amountLastIncome;
    }

    public void setAmountLastIncome(Double amountLastIncome) {
        this.amountLastIncome = amountLastIncome;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public boolean isReduceWorkHours() {
        return reduceWorkHours;
    }

    public void setReduceWorkHours(boolean reduceWorkHours) {
        this.reduceWorkHours = reduceWorkHours;
    }

    public boolean isRhtIndemnity() {
        return rhtIndemnity;
    }

    public void setRhtIndemnity(boolean rhtIndemnity) {
        this.rhtIndemnity = rhtIndemnity;
    }

    public boolean isAssimilablePosition() {
        return assimilablePosition;
    }

    public void setAssimilablePosition(boolean assimilablePosition) {
        this.assimilablePosition = assimilablePosition;
    }

    public boolean isHasLossOfProfit() {
        return hasLossOfProfit;
    }

    public void setHasLossOfProfit(boolean hasLossOfProfit) {
        this.hasLossOfProfit = hasLossOfProfit;
    }
}
