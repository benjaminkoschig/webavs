package ch.globaz.param.business.service;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Service locator pour les services publiques des paramètres
 * 
 * @author GMO
 * 
 */
public class ParamServiceLocator {
    /**
     * Fournit une instance des services des paramètres
     * 
     * @return instance de CompteIndividuelService
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static ParameterModelService getParameterModelService() throws JadeApplicationServiceNotAvailableException {
        return (ParameterModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ParameterModelService.class);
    }
}
