package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // throw (new EAVSInvalidXmlFormatException("test"));
    }

    public abstract AbstractHeader getHeader();

    public abstract AbstractContent getContent();
}
