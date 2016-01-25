package ch.globaz.cygnus.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.cygnus.business.services.pcservices.PCTransfertDossierService;

public abstract class CygnusServiceLocator {

    public static ConventionService getConventionService() throws JadeApplicationServiceNotAvailableException {
        return (ConventionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ConventionService.class);
    }

    @Deprecated
    public static PCTransfertDossierService getPCTransfertDossierService()
            throws JadeApplicationServiceNotAvailableException {
        return (PCTransfertDossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PCTransfertDossierService.class);
    }

}
