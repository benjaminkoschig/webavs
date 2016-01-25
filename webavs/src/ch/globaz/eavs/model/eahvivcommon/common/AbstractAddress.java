package ch.globaz.eavs.model.eahvivcommon.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
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

public abstract class AbstractAddress extends EahVivCommonModel implements EAVSNonFinalNode {
    public abstract AbstractAddressLine1 getAddressLine1();

    public abstract AbstractAddressLine2 getAddressLine2();

    public abstract AbstractCountry getCountry();

    public abstract AbstractDwellingNumber getDwellingNumber();

    public abstract AbstractForeignZipCode getForeignZipCode();

    public abstract AbstractHouseNumber getHouseNumber();

    public abstract AbstractLocality getLocality();

    public abstract AbstractPostOfficeBoxNumber getPostOfficeBoxNumber();

    public abstract AbstractPostOfficeBoxText getPostOfficeBoxText();

    public abstract AbstractStreet getStreet();

    public abstract AbstractSwissZipCode getSwissZipCode();

    public abstract AbstractTown getTown();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
