package ch.globaz.eavs.model.eCH0044.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public class AbstractYearMonth extends Ech0044Model implements EAVSFinalNode {
    private String value = null;

    public AbstractYearMonth() {
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
