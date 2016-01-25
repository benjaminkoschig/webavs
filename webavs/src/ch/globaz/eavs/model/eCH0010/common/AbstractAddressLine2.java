package ch.globaz.eavs.model.eCH0010.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractAddressLine2 extends Ech0010Model implements EAVSFinalNode {
    private String value = null;

    public AbstractAddressLine2() {
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
