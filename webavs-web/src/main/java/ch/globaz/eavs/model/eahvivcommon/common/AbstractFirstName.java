package ch.globaz.eavs.model.eahvivcommon.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractFirstName extends EahVivCommonModel implements EAVSFinalNode {
    private String value = null;

    public AbstractFirstName() {
        super();
    }

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
