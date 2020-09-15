package globaz.draco.properties;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.draco.application.DSApplication;

public enum DSProperties implements IProperties {

    PROPERTY_ANNEE_BONUSPFA_VARIABLE("bonusPFAVariable.annee",
            "ann�e � partir de laquelle la r�duction PFA pour les d�comptes �lectroniques se base sur un taux variable"),
    PREIMPR_SANSPERS_FOR_PERSJUR("preImpr.sansPers.for.persJur",
            "Personnalit�s juridiques (ALL/NONE/code_syst�me1,code_syst�me2...) pour lesquelles il faut pouvoir effectuer la pr�impression des DS (malgr� la particularit� sans personnel)"),
    MAJORATION_DECLARATION_MANUELLE_ACTIVE("majoration.declaration.manuelle.active",
            "Indique si la majoration des frais d'administration est active ainsi que la liste des types de d�clarations qui d�clanchent une majoration"),
    MAJORATION_DECLARATION_MANUELLE_ASSURANCE("majoration.declaration.manuelle.assurance",
            "Indique les assurances associ�es aux taux de majoration des frais d'administration");

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
