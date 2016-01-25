package ch.globaz.eavs.model.eahviv2011000104.common;

import java.util.Map;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractSubMessageType extends CommonModel implements EAVSFinalNode {
    private String value = null;

    @Override
    public void fillMetaData(Map metadata) {
        metadata.put("subType", getValue());
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
