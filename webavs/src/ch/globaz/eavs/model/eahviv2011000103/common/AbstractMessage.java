package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractContent getContent();

    public abstract AbstractHeader getHeader();

    public abstract void setContent(EAVSAbstractModel _content);

    public abstract void setHeader(EAVSAbstractModel _header);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}
