package ch.globaz.pegasus.utils;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCLoiCantonaleProperty;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.web.application.PCApplication;

public final class PCApplicationUtil {

    private static PCApplicationUtil instance;

    public static PCApplicationUtil getInstance() {
        if (PCApplicationUtil.instance == null) {
            PCApplicationUtil.instance = new PCApplicationUtil();
        }
        return PCApplicationUtil.instance;
    }

    private PCApplicationUtil() {
        super();
    }

    public String getDefaultApplicationName() {
        return PCApplication.DEFAULT_APPLICATION_PEGASUS;
    }

    public static boolean isCantonVS() throws CalculException {
        try {
            return EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC();
        } catch (PropertiesException e) {
            throw new CalculException(e.getMessage());
        }
    }

}
