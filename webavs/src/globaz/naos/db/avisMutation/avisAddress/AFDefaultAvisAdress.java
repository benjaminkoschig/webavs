package globaz.naos.db.avisMutation.avisAddress;

import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.avisMutation.AFProcessAvisSedexWriterNew;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.tiers.TITiers;

class AFDefaultAvisAdress implements AFAvisAddress {
    private String country = "";
    private String houseNumber = "";
    private String name1 = "";
    private String name2 = "";
    private String name3 = "";
    private String street = "";
    private String swissZipCode = "";
    private String town = "";

    AFDefaultAvisAdress(AFAffiliation anAffiliation, TITiers aTiers, TIAdresseDataSource dsAdresse) {
        String titreTiers = anAffiliation.getTiers().getTitreTiers();
        if (ITITiers.CS_MONSIEUR.equals(titreTiers)) {
            name1 = "1";
        } else if (ITITiers.CS_MADAME.equals(titreTiers)) {
            name1 = "2";
        } else {
            name1 = "0";
        }
        name2 = aTiers.getDesignation1();
        name3 = aTiers.getDesignation2();
        // address
        street = dsAdresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
        houseNumber = dsAdresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
        town = dsAdresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        swissZipCode = dsAdresse.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
        country = AFProcessAvisSedexWriterNew.PAYS;
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

    private String getStrReprensentation() {
        return getStreet() + getHouseNumber() + getTown() + getSwissZipCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AFDefaultAvisAdress)) {
            return false;
        }
        AFDefaultAvisAdress defAvisAd = (AFDefaultAvisAdress) obj;
        return getStrReprensentation().equals(defAvisAd.getStrReprensentation());
    }
}
