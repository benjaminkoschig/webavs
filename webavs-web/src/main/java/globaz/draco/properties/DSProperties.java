package globaz.draco.properties;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.draco.application.DSApplication;

public enum DSProperties implements IProperties {

    PROPERTY_ANNEE_BONUSPFA_VARIABLE("bonusPFAVariable.annee",
            "année à partir de laquelle la réduction PFA pour les décomptes électroniques se base sur un taux variable"),
    PREIMPR_SANSPERS_FOR_PERSJUR("preImpr.sansPers.for.persJur",
            "Personnalités juridiques (ALL/NONE/code_système1,code_système2...) pour lesquelles il faut pouvoir effectuer la préimpression des DS (malgré la particularité sans personnel)"),
    MAJORATION_DECLARATION_MANUELLE_ACTIVE("majoration.declaration.manuelle.active",
            "Indique si la majoration des frais d'administration est active ainsi que la liste des types de déclarations qui déclanchent une majoration"),
    MAJORATION_DECLARATION_MANUELLE_ASSURANCE("majoration.declaration.manuelle.assurance",
            "Indique les assurances associées aux taux de majoration des frais d'administration");

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
