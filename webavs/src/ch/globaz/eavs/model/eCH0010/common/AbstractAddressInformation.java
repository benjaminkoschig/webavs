package ch.globaz.eavs.model.eCH0010.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractAddressInformation extends Ech0010Model implements EAVSNonFinalNode {
    public abstract AbstractAddressLine1 getAddressLine1();

    public abstract AbstractAddressLine2 getAddressLine2();

    public abstract AbstractHouseNumber getHouseNumber();

    public abstract AbstractLocality getLocality();

    public abstract AbstractStreet getStreet();

    public abstract AbstractSwissZipCode getSwissZipCode();

    public abstract AbstractSwissZipCodeAddOn getSwissZipCodeAddOn();

    public abstract AbstractTown getTown();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
