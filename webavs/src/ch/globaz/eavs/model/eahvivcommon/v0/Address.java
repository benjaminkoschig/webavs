package ch.globaz.eavs.model.eahvivcommon.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressLine1;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressLine2;
import ch.globaz.eavs.model.eCH0010.common.AbstractCountry;
import ch.globaz.eavs.model.eCH0010.common.AbstractDwellingNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractForeignZipCode;
import ch.globaz.eavs.model.eCH0010.common.AbstractHouseNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractLocality;
import ch.globaz.eavs.model.eCH0010.common.AbstractPostOfficeBoxNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractPostOfficeBoxText;
import ch.globaz.eavs.model.eCH0010.common.AbstractStreet;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCode;
import ch.globaz.eavs.model.eCH0010.common.AbstractTown;
import ch.globaz.eavs.model.eCH0010.v3.AddressLine1;
import ch.globaz.eavs.model.eCH0010.v3.AddressLine2;
import ch.globaz.eavs.model.eCH0010.v3.Country;
import ch.globaz.eavs.model.eCH0010.v3.ForeignZipCode;
import ch.globaz.eavs.model.eCH0010.v3.HouseNumber;
import ch.globaz.eavs.model.eCH0010.v3.Locality;
import ch.globaz.eavs.model.eCH0010.v3.Street;
import ch.globaz.eavs.model.eCH0010.v3.SwissZipCode;
import ch.globaz.eavs.model.eCH0010.v3.Town;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractAddress;

public class Address extends AbstractAddress {
    private AddressLine1 addressLine1 = null;
    private AddressLine2 addressLine2 = null;
    private Country country = null;
    private ForeignZipCode foreignZipCode = null;
    private HouseNumber houseNumber = null;
    private Locality locality = null;
    private Street street = null;
    private SwissZipCode swissZipCode = null;
    private Town town = null;

    @Override
    public AbstractAddressLine1 getAddressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new AddressLine1();
        }
        return addressLine1;
    }

    @Override
    public AbstractAddressLine2 getAddressLine2() {
        if (addressLine2 == null) {
            addressLine2 = new AddressLine2();
        }
        return addressLine2;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(street);
        result.add(houseNumber);
        result.add(town);
        result.add(swissZipCode);
        result.add(foreignZipCode);
        result.add(country);
        result.add(addressLine1);
        result.add(addressLine2);
        result.add(locality);
        return result;
    }

    @Override
    public AbstractCountry getCountry() {
        if (country == null) {
            country = new Country();
        }
        return country;
    }

    @Override
    public AbstractDwellingNumber getDwellingNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractForeignZipCode getForeignZipCode() {
        if (foreignZipCode == null) {
            foreignZipCode = new ForeignZipCode();
        }
        return foreignZipCode;
    }

    @Override
    public AbstractHouseNumber getHouseNumber() {
        if (houseNumber == null) {
            houseNumber = new HouseNumber();
        }
        return houseNumber;
    }

    @Override
    public AbstractLocality getLocality() {
        if (locality == null) {
            locality = new Locality();
        }
        return locality;
    }

    @Override
    public AbstractPostOfficeBoxNumber getPostOfficeBoxNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractPostOfficeBoxText getPostOfficeBoxText() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractStreet getStreet() {
        if (street == null) {
            street = new Street();
        }
        return street;
    }

    @Override
    public AbstractSwissZipCode getSwissZipCode() {
        if (swissZipCode == null) {
            swissZipCode = new SwissZipCode();
        }
        return swissZipCode;
    }

    @Override
    public AbstractTown getTown() {
        if (town == null) {
            town = new Town();
        }
        return town;
    }

    public void setAddressLine1(EAVSAbstractModel _addressLine1) {
        addressLine1 = (AddressLine1) _addressLine1;
    }

    public void setAddressLine2(EAVSAbstractModel _addressLine2) {
        addressLine2 = (AddressLine2) _addressLine2;
    }

    public void setCountry(EAVSAbstractModel _country) {
        country = (Country) _country;
    }

    public void setHouseNumber(EAVSAbstractModel _houseNumber) {
        houseNumber = (HouseNumber) _houseNumber;
    }

    public void setLocality(EAVSAbstractModel _locality) {
        locality = (Locality) _locality;
    }

    public void setStreet(EAVSAbstractModel _street) {
        street = (Street) _street;
    }

    public void setSwissZipCode(EAVSAbstractModel _swissZipCode) {
        swissZipCode = (SwissZipCode) _swissZipCode;
    }

    public void setTown(EAVSAbstractModel _town) {
        town = (Town) _town;
    }
}
