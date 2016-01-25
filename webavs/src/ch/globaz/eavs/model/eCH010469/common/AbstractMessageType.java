package ch.globaz.eavs.model.eCH010469.common;

import java.util.Map;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSFinalNode;

public abstract class AbstractMessageType extends Ech010469Model implements EAVSFinalNode {
    private String value = null;

    @Override
    public void fillMetaData(Map metadata) {
        metadata.put("type", getValue());
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
