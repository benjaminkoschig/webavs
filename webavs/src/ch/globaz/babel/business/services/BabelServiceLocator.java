package ch.globaz.babel.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class BabelServiceLocator {

    public static PCCatalogueTexteService getPCCatalogueTexteService()
            throws JadeApplicationServiceNotAvailableException {
        return (PCCatalogueTexteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PCCatalogueTexteService.class);
    }

    public static PFCatalogueTexteService getPFCatalogueTexteService()
            throws JadeApplicationServiceNotAvailableException {
        return (PFCatalogueTexteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PFCatalogueTexteService.class);
    }
}
