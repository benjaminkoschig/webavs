package ch.globaz.eavs.model.eahvivcommon.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonId;
import ch.globaz.eavs.model.eCH0044.common.AbstractPersonIdCategory;

public abstract class AbstractLocalPersonId extends EahVivCommonModel implements EAVSNonFinalNode {
    public abstract AbstractPersonId getPersonId();

    public abstract AbstractPersonIdCategory getPersonIdCategory();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
