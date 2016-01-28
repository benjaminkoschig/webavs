package globaz.phenix.db.communications;

import java.io.FileWriter;
import java.io.IOException;

public class CPCommunicationsFiscalesSedexIndividu {
    public final static String CELIBATAIRE = "1";
    public final static String MARIE = "2";
    private String country = "";
    private String dateOfBirth = "";
    private String dateOfEntry = "";
    private String dateOfMaritalStatus = "";
    private String firstName = "";
    private String houseNumber = "";
    private String maritalStatus = "";
    private String officialName = "";
    private int orderScope = 0;
    private String personId = "";
    private String personIdCategory = "";
    private String sex = "";
    private CPCommunicationsFiscalesSedexIndividu spouse;
    private String street = "";
    private String swissZipCode = "";

    private String town = "";
    private String vn = "";

    public void calculeOrderScope(String genreAffilie) {
        // TODO Auto-generated method stub

    }

    public String getCountry() {
        return country;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getOfficialName() {
        return officialName;
    }

    public int getOrderScope() {
        return orderScope;
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

    public CPCommunicationsFiscalesSedexIndividu getSpouse() {
        return spouse;
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

    public void printInfos() {
        FileWriter ecriTemp = null;
        try {
            if (ecriTemp == null) {
                ecriTemp = new FileWriter("D:/Temp/Sedex.txt", true);
            }
            saisieDonnee("vn", getVn(), ecriTemp);
            saisieDonnee("officialName", getOfficialName(), ecriTemp);
            saisieDonnee("firstName", getFirstName(), ecriTemp);
            saisieDonnee("sex", getSex(), ecriTemp);
            saisieDonnee("dateOfBirth", getDateOfBirth(), ecriTemp);
            saisieDonnee("adress", getStreet() + " " + getHouseNumber(), ecriTemp);
            saisieDonnee("", getSwissZipCode() + " " + getTown() + " " + "(" + getCountry() + ")", ecriTemp);
            saisieDonnee("maritalStatus", getMaritalStatus(), ecriTemp);
            if (spouse != null) {
                saisieDonnee("spouse vn", spouse.getVn(), ecriTemp);
                saisieDonnee("spouse officialName", spouse.getOfficialName(), ecriTemp);
                saisieDonnee("spouse firstName", spouse.getFirstName(), ecriTemp);
                saisieDonnee("spouse sex", spouse.getSex(), ecriTemp);
                saisieDonnee("spouse dateOfBirth", spouse.getDateOfBirth(), ecriTemp);
                saisieDonnee("spouse adress", spouse.getStreet() + " " + spouse.getHouseNumber(), ecriTemp);
                saisieDonnee("", spouse.getSwissZipCode() + " " + spouse.getTown() + " " + "(" + spouse.getCountry()
                        + ")", ecriTemp);
                saisieDonnee("spouse maritalStatus", spouse.getMaritalStatus(), ecriTemp);
            }

        }// try
        catch (NullPointerException a) {
            System.out.println("Erreur : pointeur null");
        } catch (IOException a) {
            System.out.println("Problème d'IO");
        } finally {
            try {
                if (ecriTemp != null) {
                    ecriTemp.close();
                }
            } catch (IOException e) {
                System.out.println("Problème d'IO");
            }
        }

    }

    private void saisieDonnee(String nomElement, String text, FileWriter ecriTemp) throws IOException {
        text += "\r\n";
        ecriTemp.write(nomElement + " : " + text, 0, (nomElement + " : " + text).length());
        ecriTemp.flush();
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setOrderScope(int orderScope) {
        this.orderScope = orderScope;
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

    public void setSpouse(CPCommunicationsFiscalesSedexIndividu spouse) {
        this.spouse = spouse;
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
}
