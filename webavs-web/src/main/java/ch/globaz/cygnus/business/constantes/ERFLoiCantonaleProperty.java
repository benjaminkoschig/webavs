package ch.globaz.cygnus.business.constantes;

import ch.globaz.common.properties.PropertiesException;

public enum ERFLoiCantonaleProperty {
    JURA("JU"),
    VAUD("VD"),
    VALAIS("VS");

    private ERFLoiCantonaleProperty(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public Boolean isLoiCantonRFM() throws PropertiesException {
        return getValue().equalsIgnoreCase(ERFProperties.LOI_CANTONALE_RFM.getValue());
    }

}
