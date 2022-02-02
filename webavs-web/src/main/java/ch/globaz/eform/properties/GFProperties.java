package ch.globaz.eform.properties;

import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.web.application.GFApplication;

public enum GFProperties implements IProperties {
    APPLICATION_NAME("applicationName", "Nom de l'application eFormulaire"),
    APPLICATION_MAIN_CLASS("applicationClassName","Nom de la classe principale de l'application eFromulaire."),
    EMAIL_EFORM("email.sedex.validation", "Email pour l'envoi des fichiers sedex P14 non traités");

    private final String description;
    private final String propertyName;

    GFProperties(final String propertyName, final String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return GFApplication.APPLICATION_NAME;
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
