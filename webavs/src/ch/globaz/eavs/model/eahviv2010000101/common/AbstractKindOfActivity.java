package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractKindOfActivity extends CommonModel implements EAVSFinalNode {
    private String value = null;

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
