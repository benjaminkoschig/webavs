package globaz.musca.constantes;

import globaz.musca.application.FAApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum EFAProperties implements IProperties {
    FACTUARTION_MULTISHEET("facturation.separateMultiSheets"),
    USE_USER_SESSION_FOR_HEADER("facturation.user.session.header");

    private String property;

    EFAProperties(String property) {
        this.property = property;
    }

    @Override
    public String getApplicationName() {
        return FAApplication.DEFAULT_APPLICATION_MUSCA;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return null;
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
