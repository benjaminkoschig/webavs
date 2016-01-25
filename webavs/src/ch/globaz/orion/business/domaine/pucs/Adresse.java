package ch.globaz.orion.business.domaine.pucs;

public class Adresse {

    private String street;
    private String zipCode;
    private String city;

    public Adresse(String street, String zipCode, String city) {
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

}
