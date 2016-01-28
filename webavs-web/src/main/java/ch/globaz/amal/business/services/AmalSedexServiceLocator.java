package ch.globaz.amal.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.services.sedexRP.AnnoncesRPService;

public class AmalSedexServiceLocator {
    public static AnnoncesRPService getAnnoncesRPService() throws JadeApplicationServiceNotAvailableException {
        return (AnnoncesRPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AnnoncesRPService.class);
    }
}
