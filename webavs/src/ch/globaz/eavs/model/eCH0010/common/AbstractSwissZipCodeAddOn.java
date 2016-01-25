package ch.globaz.eavs.model.eCH0010.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractSwissZipCodeAddOn extends Ech0010Model implements EAVSFinalNode {
    private String value = null;

    public AbstractSwissZipCodeAddOn() {
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
