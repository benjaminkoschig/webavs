package ch.globaz.al.businessimpl.rafam.office;

import globaz.globall.api.GlobazSystem;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.web.application.ALApplication;
import ch.globaz.common.properties.PropertiesException;

public class OfficeFactory {

    public static Office instanciate(String numAffilie, String activiteAllocataire) throws JadeApplicationException {

        return isMultiCaisse() ? new MultiCaisseOffice(numAffilie, activiteAllocataire) : new UniqueCaisseOffice();
    }

    private static boolean isMultiCaisse() throws JadeApplicationException {

        String isMulticaisseValue;
        try {
            isMulticaisseValue = GlobazSystem.getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF).getProperty(
                    ALConstRafam.RAFAM_IS_MULTI_CAISSE);
        } catch (Exception e) {
            throw new PropertiesException("Unable to retrieve the property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_IS_MULTI_CAISSE + "]", e);
        }

        if (null == isMulticaisseValue) {
            throw new PropertiesException("The property [" + ALApplication.APPLICATION_NAME + "."
                    + ALConstRafam.RAFAM_IS_MULTI_CAISSE + "] doesn't exist");
        }

        if ("true".equalsIgnoreCase(isMulticaisseValue) || "false".equalsIgnoreCase(isMulticaisseValue)) {
            return Boolean.parseBoolean(isMulticaisseValue);
        } else {
            throw new PropertiesException("The value (" + isMulticaisseValue + ") for the properties "
                    + ALConstRafam.RAFAM_IS_MULTI_CAISSE + " is not a boolean value");
        }
    }
}
