package ch.globaz.osiris.business.constantes;

import globaz.osiris.application.CAApplication;
import java.util.List;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum CAProperties implements IProperties {
    RUBRIQUE_SANS_EXTOURNE("rubrique.sansExtourne", "Liste des rubriques pour lesquelles on extourne par les écritures"),
    ISO_SEPA_MAX_MULTIOG("iso.sepa.nbmax.multiog", "nombre max d'OG créé à la préparation"),
    ISO_SEPA_MAX_OVPAROG("iso.sepa.nbmax.ovparog", "nombre max d'OV par OG à la préparation");

    private String description;
    private String propertyName;

    CAProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return CAApplication.DEFAULT_APPLICATION_OSIRIS;
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

    public List<String> getListValue() throws PropertiesException {
        return CommonPropertiesUtils.getListValue(this);
    }
}
