package ch.globaz.eform.business;

import ch.globaz.eform.business.services.GFAdministrationService;
import ch.globaz.eform.business.services.GFDaDossierDBService;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.business.services.GFEFormDBService;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class GFEFormServiceLocator {
    public static GFEFormDBService getGFEFormDBService() throws JadeApplicationServiceNotAvailableException {
        return (GFEFormDBService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFEFormDBService.class);
    }

    public static GFDaDossierDBService getGFDaDossierDBService() throws JadeApplicationServiceNotAvailableException {
        return (GFDaDossierDBService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFDaDossierDBService.class);
    }

    public static GFDaDossierSedexService getGFDaDossierSedexService() throws JadeApplicationServiceNotAvailableException {
        return (GFDaDossierSedexService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFDaDossierSedexService.class);
    }

    public static GFAdministrationService getGFAdministrationService() throws JadeApplicationServiceNotAvailableException {
        return (GFAdministrationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(GFAdministrationService.class);
    }
}