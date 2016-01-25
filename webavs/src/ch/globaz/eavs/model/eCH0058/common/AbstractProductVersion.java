package ch.globaz.eavs.model.eCH0058.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractProductVersion extends Ech0058Model implements EAVSFinalNode {
    private String value = null;

    public AbstractProductVersion() {
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
        if (value == null) {
            // rien de setté, rien à contrôler
            return;
        }
        if (value.length() > 10) {
            throw new EAVSInvalidXmlFormatException(this.getClass().getName()
                    + " should be more than 10 chars. Actual value: " + value);
        }
    }

}
