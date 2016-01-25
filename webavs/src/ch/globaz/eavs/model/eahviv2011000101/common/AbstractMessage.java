package ch.globaz.eavs.model.eahviv2011000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends CommonModel implements EAVSNonFinalNode {
    private boolean isRoot = true;

    public void deactivateRoot() {
        isRoot = false;
    }

    @Override
    public boolean generateNameSpace() {
        return isRoot;
    }

    public abstract AbstractContent getContent();

    public abstract AbstractHeader getHeader();

    @Override
    protected String overrideMinorVersion() {
        return " " + MINORVERSION;
    }

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // throw (new EAVSInvalidXmlFormatException("test"));
    }
}
