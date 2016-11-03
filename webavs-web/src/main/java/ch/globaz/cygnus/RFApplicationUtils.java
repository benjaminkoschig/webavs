package ch.globaz.cygnus;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.cygnus.business.constantes.ERFLoiCantonaleProperty;

public class RFApplicationUtils {

    private static RFApplicationUtils instance;
    public final static String DEFAULT_APPLICATION_CYGNUS = "CYGNUS";

    public static RFApplicationUtils getInstance() {
        if (RFApplicationUtils.instance == null) {
            RFApplicationUtils.instance = new RFApplicationUtils();
        }
        return RFApplicationUtils.instance;
    }

    private RFApplicationUtils() {
        super();
    }

    public String getDefaultApplicationName() {
        return DEFAULT_APPLICATION_CYGNUS;
    }

    public static boolean isCantonVS() throws PropertiesException {
        try {
            return ERFLoiCantonaleProperty.VALAIS.isLoiCantonRFM();
        } catch (PropertiesException e) {
            throw new PropertiesException(e.getMessage());
        }
    }
}
