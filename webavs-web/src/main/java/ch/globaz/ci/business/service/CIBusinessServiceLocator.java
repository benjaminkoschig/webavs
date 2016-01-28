package ch.globaz.ci.business.service;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Service locator pour les services publiques des CI
 * 
 * @author GMO
 * 
 */
public class CIBusinessServiceLocator {
    /**
     * Fournit une instance des services des CI
     * 
     * @return instance de CompteIndividuelService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static CompteIndividuelService getCompteIndividuelService()
            throws JadeApplicationServiceNotAvailableException {
        return (CompteIndividuelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CompteIndividuelService.class);
    }
}
