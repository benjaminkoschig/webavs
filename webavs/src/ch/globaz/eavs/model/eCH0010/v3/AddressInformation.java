package ch.globaz.eavs.model.eCH0010.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressInformation;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressLine1;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressLine2;
import ch.globaz.eavs.model.eCH0010.common.AbstractHouseNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractLocality;
import ch.globaz.eavs.model.eCH0010.common.AbstractStreet;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCode;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCodeAddOn;
import ch.globaz.eavs.model.eCH0010.common.AbstractTown;

public class AddressInformation extends AbstractAddressInformation implements EAVSNonFinalNode {
    private AddressLine1 addressLine1 = null;
    private AddressLine2 addressLine2 = null;
    private HouseNumber houseNumber = null;
    private Locality locality = null;
    private Street street = null;
    private SwissZipCode swissZipCode = null;
    private SwissZipCodeAddOn swissZipCodeAddOn = null;
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
        result.add(addressLine1);
        result.add(addressLine2);
        result.add(street);
        result.add(houseNumber);
        result.add(locality);
        result.add(town);
        result.add(swissZipCode);
        result.add(swissZipCodeAddOn);
        return result;
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
    public AbstractSwissZipCodeAddOn getSwissZipCodeAddOn() {
        if (swissZipCodeAddOn == null) {
            swissZipCodeAddOn = new SwissZipCodeAddOn();
        }
        return swissZipCodeAddOn;
    }

    @Override
    public AbstractTown getTown() {
        if (town == null) {
            town = new Town();
        }
        return town;
    }
}
