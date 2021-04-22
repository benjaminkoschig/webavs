package ch.globaz.common.businessimpl;

import ch.globaz.common.business.services.ParametreService;
import ch.globaz.common.business.services.UPIService;
import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class CommonServiceLocator {
    public static UPIService getUPIService() throws JadeApplicationServiceNotAvailableException {
        return (UPIService) JadeApplicationServiceLocator.getInstance().getServiceImpl(UPIService.class);
    }

    public static ParametreService getParametreService() {
        try {
            return (ParametreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ParametreService.class);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CommonTechnicalException("ParametreService not valide", e);
        }
    }
}
