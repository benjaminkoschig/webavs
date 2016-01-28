package globaz.naos.db.avisMutation.avisAddress;

class AFEmptyAvisAdress implements AFAvisAddress {
    private String country = "";
    private String houseNumber = "";
    private String name1 = "";
    private String name2 = "";
    private String name3 = "";
    private String street = "";
    private String swissZipCode = "";
    private String town = "";

    AFEmptyAvisAdress() {
        // it's okay, do nothing here
    }

    /**
     * @return the country
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * @return the houseNumber
     */
    @Override
    public String getHouseNumber() {
        return houseNumber;
    }

    @Override
    public String getName1() {
        return name1;
    }

    @Override
    public String getName2() {
        return name2;
    }

    @Override
    public String getName3() {
        return name3;
    }

    /**
     * @return the street
     */
    @Override
    public String getStreet() {
        return street;
    }

    /**
     * @return the swissZipCode
     */
    @Override
    public String getSwissZipCode() {
        return swissZipCode;
    }

    /**
     * @return the town
     */
    @Override
    public String getTown() {
        return town;
    }
}
