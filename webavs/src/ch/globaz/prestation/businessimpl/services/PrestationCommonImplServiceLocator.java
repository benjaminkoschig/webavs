package ch.globaz.prestation.businessimpl.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;
import ch.globaz.prestation.business.services.models.demande.SimpleDemandePrestationService;
import ch.globaz.prestation.business.services.models.echeance.EcheanceService;
import ch.globaz.prestation.business.services.models.echeance.SimpleEcheanceService;

public class PrestationCommonImplServiceLocator extends PrestationCommonServiceLocator {

    public static SimpleDemandePrestationService getSimpleDemandePrestationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleDemandePrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleDemandePrestationService.class);
    }

    public static SimpleEcheanceService getSimpleEcheanceService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleEcheanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleEcheanceService.class);
    }

    public static EcheanceService getEcheanceService() throws JadeApplicationServiceNotAvailableException {
        return (EcheanceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(EcheanceService.class);
    }

}
