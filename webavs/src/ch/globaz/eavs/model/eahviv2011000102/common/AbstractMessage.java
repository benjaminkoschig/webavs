package ch.globaz.eavs.model.eahviv2011000102.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractContent getContent();

    public abstract AbstractHeader getHeader();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // throw (new EAVSInvalidXmlFormatException("test"));
    }
}
