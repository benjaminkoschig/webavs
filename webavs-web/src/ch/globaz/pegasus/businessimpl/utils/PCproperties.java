package ch.globaz.pegasus.businessimpl.utils;

import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;

public class PCproperties {

    public static Boolean getBoolean(EPCProperties properties) throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(properties);
    }

    public static String getProperties(EPCProperties properties) throws PropertiesException {
        return CommonPropertiesUtils.getValue(properties);
    }
}
