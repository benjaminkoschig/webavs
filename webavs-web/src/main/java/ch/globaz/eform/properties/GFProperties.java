package ch.globaz.eform.properties;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.web.application.GFApplication;

public enum GFProperties implements IProperties {
    APPLICATION_NAME("applicationName", "Nom de l'application eFormulaire"),
    APPLICATION_MAIN_CLASS("applicationClassName", "Nom de la classe principale de l'application eFromulaire."),
    EMAIL_EFORM("email.sedex.validation", "Email pour l'envoi des fichiers sedex P14 non trait�s"),
    EMAIL_BODY_ERROR("email.sedex.error", "Email d'erreur pour le traitement sedex P14 en erreur"),
    GESTIONNAIRE_USER_DEFAULT("user.gestionnaire.default", "Gestionnaire par d�faut"),
    EMAIl_RECAP_FORMULAIRE("email.recap.formulaire", "Email d'envoie du fichier r�capitulatif des formulaires au statut re�u");

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
