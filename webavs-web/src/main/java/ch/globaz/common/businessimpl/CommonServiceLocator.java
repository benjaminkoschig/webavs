package ch.globaz.common.businessimpl;

import ch.globaz.common.business.services.UPIService;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class CommonServiceLocator {
    public static UPIService getUPIService() throws JadeApplicationServiceNotAvailableException {
        return (UPIService) JadeApplicationServiceLocator.getInstance().getServiceImpl(UPIService.class);
    }
}
