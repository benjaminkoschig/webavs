package ch.globaz.naos.business.service;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public abstract class AFBusinessServiceLocator {
    /**
     * @return Implémentation du service AffiliationComplex
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static AffiliationComplexService getAffiliationComplexService()
            throws JadeApplicationServiceNotAvailableException {
        return (AffiliationComplexService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                AffiliationComplexService.class);
    }

    public static AffiliationService getAffiliationService() throws JadeApplicationServiceNotAvailableException {
        return (AffiliationService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(AffiliationService.class);
    }

    public static AssuranceService getAssuranceService() throws JadeApplicationServiceNotAvailableException {
        return (AssuranceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AssuranceService.class);
    }
}
