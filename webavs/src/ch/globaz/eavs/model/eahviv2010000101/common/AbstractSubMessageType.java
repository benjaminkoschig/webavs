package ch.globaz.eavs.model.eahviv2010000101.common;

import java.util.Map;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractSubMessageType extends CommonModel implements EAVSFinalNode {
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

    @Override
    public void fillMetaData(Map metadata) {
        metadata.put("subType", getValue());
    }
}
