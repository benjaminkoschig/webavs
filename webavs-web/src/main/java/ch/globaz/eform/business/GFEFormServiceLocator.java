package ch.globaz.eform.business;

import ch.globaz.eform.business.services.GFDaDossierDBService;
import ch.globaz.eform.business.services.GFEFormDBService;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class GFEFormServiceLocator {
    public static GFEFormDBService gfEFormService() throws JadeApplicationServiceNotAvailableException {
        return (GFEFormDBService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFEFormDBService.class);
    }

    public static GFDaDossierDBService gfDaDossierDBService () throws JadeApplicationServiceNotAvailableException {
        return (GFDaDossierDBService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFDaDossierDBService.class);
    }
}
