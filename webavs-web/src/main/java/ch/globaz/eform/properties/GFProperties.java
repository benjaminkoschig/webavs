package ch.globaz.eform.properties;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.web.application.GFApplication;

public enum GFProperties implements IProperties {
    APPLICATION_NAME("applicationName", "Nom de l'application eFormulaire"),
    APPLICATION_MAIN_CLASS("applicationClassName", "Nom de la classe principale de l'application eFromulaire."),
    EMAIL_EFORM("email.sedex.validation", "Email pour l'envoi des fichiers sedex P14 non traités"),
    EMAIL_DADOSSIER("dadossier.email.sedex.retour.erreur", "Email pour l'envoi des fichiers sedex da-dossier non traités"),
    EMAIL_BODY_ERROR("email.sedex.error", "Email d'erreur pour le traitement sedex P14 en erreur"),
    EMAIL_DADOSSIER_BODY_ERROR("dadossier.email.sedex.error", "Email d'erreur pour le traitement sedex dadossier en erreur"),
    GESTIONNAIRE_USER_DEFAULT("user.gestionnaire.default", "Gestionnaire par défaut"),
    EMAIL_RECAP_FORMULAIRE("email.recap.formulaire", "Email d'envoie du fichier récapitulatif des formulaires au statut reçu"),
    GESTIONNAIRE_USER_DEPARTEMENT("dadossier.envoi.departement", "Departement du gestionnaire"),
    GESTIONNAIRE_USER_TELEPHONE("dadossier.envoi.telephone", "Telephone du gestionnaire"),
    DA_DOSSIER_MODE_TEST("dadossier.mode.test", "Identification de livraison en test"),
    SENDER_ID("sedex.sender.id", "Identifiant Sedex");

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
