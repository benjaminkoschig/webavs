package globaz.amal.properties;

import globaz.corvus.application.REApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum AMProperties implements IProperties {
    APPLICATION_DATE("applicationDate", "(Description à compléter)"),
    APPLICATION_MAIN_CLASS("applicationClassName",
            "Nom de la classe principale de l'application AMAL. Encore d'actualité ????"),
    APPLICATION_NAME("applicationName", "Nom de l'application amal");

    private String description;
    private String propertyName;

    AMProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
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
