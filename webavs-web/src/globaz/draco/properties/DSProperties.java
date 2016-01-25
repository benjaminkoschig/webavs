package globaz.draco.properties;

import globaz.draco.application.DSApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum DSProperties implements IProperties {

    PROPERTY_ANNEE_BONUSPFA_VARIABLE("bonusPFAVariable.annee",
            "année à partir de laquelle la réduction PFA pour les décomptes électroniques se base sur un taux variable");

    private String description;
    private String propertyName;

    DSProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return DSApplication.DEFAULT_APPLICATION_DRACO;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

}
