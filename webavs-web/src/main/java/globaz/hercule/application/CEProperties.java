package globaz.hercule.application;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum CEProperties implements IProperties {

    CODE_NOGA("codeNoga", "codeNoga", Boolean.class);

    private String property;
    private String description;
    private Class<?> type;

    CEProperties(String prop, String description, Class<?> type) {
        property = prop;
        this.type = type;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return CEApplication.DEFAULT_APPLICATION_HERCULE;
    };

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
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

    public Class<?> getValueType() {
        return type;
    }

}
