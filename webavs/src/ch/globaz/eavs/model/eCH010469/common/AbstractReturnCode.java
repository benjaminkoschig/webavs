package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractReturnCode extends Ech010469Model implements EAVSFinalNode {
    private String value = null;

    public AbstractReturnCode() {
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
        // TODO Auto-generated method stub

    }

}
