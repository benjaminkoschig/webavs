package ch.globaz.lyra.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class LYServiceLocator {

    public static LYEcheanceService getEcheancesService() throws JadeApplicationServiceNotAvailableException {
        return (LYEcheanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LYEcheanceService.class);
    }

    public static LYHistoriqueEcheancesService getHistoriqueEcheancesService()
            throws JadeApplicationServiceNotAvailableException {
        return (LYHistoriqueEcheancesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                LYHistoriqueEcheancesService.class);
    }
}
