package globaz.phenix.util;

import globaz.phenix.application.CPApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * Permet la récupération des propriétés les cotisations personnelles
 * 
 * @author sco
 */
public enum CPProperties implements IProperties {

    LISTE_DECISIONS_COMPTABILISEES_TYPE_SUIVI_AF("liste.decisions.comptabilisees.type.suivi.af"),
    WIRRWEBSERVICE_URI_WSDL("wirr.webservice.uri.wsdl"),
    WIRRWEBSERVICE_NAMESPACE("wirr.webservice.namespace"),
    WIRRWEBSERVICE_NAME("wirr.webservice.name"),
    WIRRWEBSERVICE_SEDEX_SENDER_ID("wirr.webservice.sedex.sender.id");

    private String property;

    CPProperties(String prop) {
        property = prop;
    }

    @Override
    public String getApplicationName() {
        return CPApplication.DEFAULT_APPLICATION_PHENIX;
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
