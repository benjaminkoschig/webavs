package ch.globaz.eavs.model.eahviv2010000101.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eCH0010.common.AbstractCountry;
import ch.globaz.eavs.model.eCH0010.common.AbstractHouseNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractStreet;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCode;
import ch.globaz.eavs.model.eCH0010.common.AbstractTown;
import ch.globaz.eavs.model.eCH0010.v3.Country;
import ch.globaz.eavs.model.eCH0010.v3.HouseNumber;
import ch.globaz.eavs.model.eCH0010.v3.Street;
import ch.globaz.eavs.model.eCH0010.v3.SwissZipCode;
import ch.globaz.eavs.model.eCH0010.v3.Town;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractAddress;

public class Address extends AbstractAddress {
    private AbstractStreet street = null;
    private AbstractHouseNumber houseNumber = null;
    private AbstractTown town = null;
    private AbstractSwissZipCode swissZipCode = null;
    private AbstractCountry country = null;

    @Override
    public AbstractStreet getStreet() {
        if (street == null) {
            street = new Street();
        }
        return street;
    }

    public void setStreet(AbstractStreet street) {
        this.street = street;
    }

    @Override
    public AbstractHouseNumber getHouseNumber() {
        if (houseNumber == null) {
            houseNumber = new HouseNumber();
        }
        return houseNumber;
    }

    public void setHouseNumber(AbstractHouseNumber houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public AbstractTown getTown() {
        if (town == null) {
            town = new Town();
        }
        return town;
    }

    public void setTown(AbstractTown town) {
        this.town = town;
    }

    @Override
    public AbstractSwissZipCode getSwissZipCode() {
        if (swissZipCode == null) {
            swissZipCode = new SwissZipCode();
        }
        return swissZipCode;
    }

    public void setSwissZipCode(AbstractSwissZipCode swissZipCode) {
        this.swissZipCode = swissZipCode;
    }

    @Override
    public AbstractCountry getCountry() {
        if (country == null) {
            country = new Country();
        }
        return country;
    }

    public void setCountry(AbstractCountry country) {
        this.country = country;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(street);
        result.add(houseNumber);
        result.add(town);
        result.add(swissZipCode);
        result.add(country);
        return result;
    }
}