package ch.globaz.al.properties;

import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum ALProperties implements IProperties {
    /**
     * Utilisé pour activer des fonctionnalités du module AF
     */
    DECISION_FILE_ATTENTE("decision.file.attente", "Active l'impression des décisions par batch") {

        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }

    },
    REVENUS_MINIMAUX("revenus.minimaux", "Liste représentant les couples années:revenu minimal") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },
    RECAPAF_MANAGE_RECAP_IN_EBUSINESS("recapaf.manage.recap.in.ebusiness",
            "Indique si les récaps AF doivent être créées dans l'EBusiness lors de la génération globale fictive") {
        @Override
        public boolean isValidValue(String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    };

    private String description;
    private String propertyName;

    ALProperties(final String propertyName, final String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    /**
     * Déterminer si la valeur renseignée dans la property est correcte
     * 
     * @param propertyValue
     *            La valeur à évaluer
     * @return <code>true</code> si la valeur de la propriété est correcte
     */
    public abstract boolean isValidValue(final String propertyValue);

    @Override
    public String getApplicationName() {
        return ALApplication.APPLICATION_NAME;
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
