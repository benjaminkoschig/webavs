package ch.globaz.eavs.model.eahviv2011000104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractRecipientId extends CommonModel implements EAVSFinalNode {
    private String value = null;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}
