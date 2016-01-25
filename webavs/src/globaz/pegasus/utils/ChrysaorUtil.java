package globaz.pegasus.utils;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;

public class ChrysaorUtil {

    public static boolean isChrysaorEnabled() throws PropertiesException {
        return PCproperties.getBoolean(EPCProperties.CHRYSAOR);
    }
}
