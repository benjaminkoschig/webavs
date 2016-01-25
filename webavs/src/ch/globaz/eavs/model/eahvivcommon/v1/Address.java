package ch.globaz.eavs.model.eahvivcommon.v1;

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
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCodeAddOn;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCodeId;
import ch.globaz.eavs.model.eCH0010.common.AbstractTown;
import ch.globaz.eavs.model.eCH0010.v3.AddressLine1;
import ch.globaz.eavs.model.eCH0010.v3.AddressLine2;
import ch.globaz.eavs.model.eCH0010.v3.Country;
import ch.globaz.eavs.model.eCH0010.v3.DwellingNumber;
import ch.globaz.eavs.model.eCH0010.v3.ForeignZipCode;
import ch.globaz.eavs.model.eCH0010.v3.HouseNumber;
import ch.globaz.eavs.model.eCH0010.v3.Locality;
import ch.globaz.eavs.model.eCH0010.v3.PostOfficeBoxNumber;
import ch.globaz.eavs.model.eCH0010.v3.PostOfficeBoxText;
import ch.globaz.eavs.model.eCH0010.v3.Street;
import ch.globaz.eavs.model.eCH0010.v3.SwissZipCode;
import ch.globaz.eavs.model.eCH0010.v3.SwissZipCodeAddOn;
import ch.globaz.eavs.model.eCH0010.v3.SwissZipCodeId;
import ch.globaz.eavs.model.eCH0010.v3.Town;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractAddress;

public class Address extends AbstractAddress {
    private AddressLine1 addressLine1 = null;
    private AddressLine2 addressLine2 = null;
    private Country country = null;
    private DwellingNumber dwellingNumber = null;
    private ForeignZipCode foreignZipCode = null;
    private HouseNumber houseNumber = null;
    private Locality locality = null;
    private PostOfficeBoxNumber postOfficeBoxNumber = null;
    private PostOfficeBoxText postOfficeBoxText = null;
    private Street street = null;
    private SwissZipCode swissZipCode = null;
    private SwissZipCodeAddOn swissZipCodeAddOn = null;
    private SwissZipCodeId swissZipCodeId = null;
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
        result.add(swissZipCodeId);
        result.add(swissZipCodeAddOn);
        result.add(foreignZipCode);
        result.add(country);
        result.add(addressLine1);
        result.add(addressLine2);
        result.add(locality);
        result.add(dwellingNumber);
        result.add(postOfficeBoxNumber);
        result.add(postOfficeBoxText);
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
        if (dwellingNumber == null) {
            dwellingNumber = new DwellingNumber();
        }
        return dwellingNumber;
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
        if (postOfficeBoxNumber == null) {
            postOfficeBoxNumber = new PostOfficeBoxNumber();
        }
        return postOfficeBoxNumber;
    }

    @Override
    public AbstractPostOfficeBoxText getPostOfficeBoxText() {
        if (postOfficeBoxText == null) {
            postOfficeBoxText = new PostOfficeBoxText();
        }
        return postOfficeBoxText;
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

    public AbstractSwissZipCodeAddOn getSwissZipCodeAddOn() {
        if (swissZipCodeAddOn == null) {
            swissZipCodeAddOn = new SwissZipCodeAddOn();
        }
        return swissZipCodeAddOn;
    }

    public AbstractSwissZipCodeId getSwissZipCodeId() {
        if (swissZipCodeId == null) {
            swissZipCodeId = new SwissZipCodeId();
        }
        return swissZipCodeId;
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

    public void setDwellingNumber(EAVSAbstractModel _dwellingNumber) {
        dwellingNumber = (DwellingNumber) _dwellingNumber;
    }

    public void setHouseNumber(EAVSAbstractModel _houseNumber) {
        houseNumber = (HouseNumber) _houseNumber;
    }

    public void setLocality(EAVSAbstractModel _locality) {
        locality = (Locality) _locality;
    }

    public void setPostOfficeBoxNumber(EAVSAbstractModel _postOfficeBoxNumber) {
        postOfficeBoxNumber = (PostOfficeBoxNumber) _postOfficeBoxNumber;
    }

    public void setPostOfficeBoxText(EAVSAbstractModel _postOfficeBoxText) {
        postOfficeBoxText = (PostOfficeBoxText) _postOfficeBoxText;
    }

    public void setStreet(EAVSAbstractModel _street) {
        street = (Street) _street;
    }

    public void setSwissZipCode(EAVSAbstractModel _swissZipCode) {
        swissZipCode = (SwissZipCode) _swissZipCode;
    }

    public void setSwissZipCodeAddOn(EAVSAbstractModel _swissZipCodeAddOn) {
        swissZipCodeAddOn = (SwissZipCodeAddOn) _swissZipCodeAddOn;
    }

    public void setSwissZipCodeId(EAVSAbstractModel _swissZipCodeId) {
        swissZipCodeId = (SwissZipCodeId) _swissZipCodeId;
    }

    public void setTown(EAVSAbstractModel _town) {
        town = (Town) _town;
    }
}
