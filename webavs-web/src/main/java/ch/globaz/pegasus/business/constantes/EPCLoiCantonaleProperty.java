package ch.globaz.pegasus.business.constantes;

import ch.globaz.common.properties.PropertiesException;

public enum EPCLoiCantonaleProperty {
    JURA("JU"),
    VAUD("VD"),
    VALAIS("VS");

    private EPCLoiCantonaleProperty(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public Boolean isLoiCantonPC() throws PropertiesException {
        return getValue().equalsIgnoreCase(EPCProperties.LOI_CANTONALE_PC.getValue());
    }

}
