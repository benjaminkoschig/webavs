package ch.globaz.cygnus.business.constantes;

import globaz.cygnus.application.RFApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum ERFProperties implements IProperties {
    PROPERTY_VERIFIER_ATTESTATION_TAXI("verificationAttestation.sousTypesSoins.attestation.ajouter.taxi"),
    PROPERTY_VERIFIER_ATTESTATION_LIT("verificationAttestation.sousTypesSoins.attestation.ajouter.lit"),
    GESTION_TEXTE_REDIRECTION("gestion.texte.redirection"),
    VERIFIER_ATTESTATION_MAINTIEN_DOMICILE_CONROLE_NOMBRE_HEURE(
            "verificationAttestation.maintienDomicile.demande.controleNombreHeure");

    private String property;

    ERFProperties(String prop) {
        property = prop;
    }

    @Override
    public String getApplicationName() {
        return RFApplication.DEFAULT_APPLICATION_CYGNUS;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String getPropertyName() {
        return property;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

}
