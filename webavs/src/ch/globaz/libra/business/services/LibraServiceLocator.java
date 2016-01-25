package ch.globaz.libra.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class LibraServiceLocator {

    public static DossierService getDossierService() throws JadeApplicationServiceNotAvailableException {
        return (DossierService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DossierService.class);
    }

    public static EcheancesService getEcheanceService() throws JadeApplicationServiceNotAvailableException {
        return (EcheancesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(EcheancesService.class);
    }

    public static JournalisationService getJournalisationService() throws JadeApplicationServiceNotAvailableException {
        return (JournalisationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                JournalisationService.class);
    }

}
