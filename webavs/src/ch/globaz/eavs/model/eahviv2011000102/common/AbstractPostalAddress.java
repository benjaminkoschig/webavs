package ch.globaz.eavs.model.eahviv2011000102.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressInformation;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisation;

public abstract class AbstractPostalAddress extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAddressInformation getAddressInformation();

    public abstract AbstractOrganisation getOrganisation();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}
