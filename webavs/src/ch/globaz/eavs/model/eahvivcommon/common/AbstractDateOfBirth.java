package ch.globaz.eavs.model.eahvivcommon.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0044.common.AbstractYearMonthDay;

public abstract class AbstractDateOfBirth extends EahVivCommonModel implements EAVSNonFinalNode {
    public abstract AbstractYearMonthDay getYearMonthDay();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
