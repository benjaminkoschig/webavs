package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0010.common.AbstractCountry;
import ch.globaz.eavs.model.eCH0010.common.AbstractHouseNumber;
import ch.globaz.eavs.model.eCH0010.common.AbstractStreet;
import ch.globaz.eavs.model.eCH0010.common.AbstractSwissZipCode;
import ch.globaz.eavs.model.eCH0010.common.AbstractTown;

public abstract class AbstractReturnAddress extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractStreet getStreet();

    public abstract AbstractHouseNumber getHouseNumber();

    public abstract AbstractTown getTown();

    public abstract AbstractSwissZipCode getSwissZipCode();

    public abstract AbstractCountry getCountry();
}
