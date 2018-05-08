package ch.globaz.al.properties;

import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum ALProperties implements IProperties {
    /**
     * Utilis� pour activer des fonctionnalit�s du module AF
     */
    DECISION_FILE_ATTENTE("decision.file.attente", "Active l'impression des d�cisions par batch") {

        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }

    },
    REVENUS_MINIMAUX("revenus.minimaux", "Liste repr�sentant les couples ann�es:revenu minimal") {
        @Override
        public boolean isValidValue(final String propertyValue) {
            return CommonPropertiesUtils.isValidBooleanPropertyValue(propertyValue);
        }
    },
    RECAPAF_MANAGE_RECAP_IN_EBUSINESS("recapaf.manage.recap.in.ebusiness",
            "Indique si les r�caps AF doivent �tre cr��es dans l'EBusiness lors de la g�n�ration globale fictive") {
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
     * D�terminer si la valeur renseign�e dans la property est correcte
     * 
     * @param propertyValue
     *            La valeur � �valuer
     * @return <code>true</code> si la valeur de la propri�t� est correcte
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
