package ch.globaz.musca.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.musca.business.services.models.passage.PassageModelService;
import ch.globaz.musca.business.services.models.passage.PassageModuleComplexModelService;

/**
 * Service locator pour les services publiques de la facturation (musca)
 * 
 * 
 */
public abstract class FABusinessServiceLocator {
    /**
     * Obtient une instance de l'implémentation des services <code>PassageModelService</code>
     * 
     * @return <code>PassageModelService</code> implementation instance
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PassageModelService getPassageModelService() throws JadeApplicationServiceNotAvailableException {
        return (PassageModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PassageModelService.class);
    }

    /**
     * Obtient une instance de l'implémentation des services <code>PassageModuleComplexModelService</code>
     * 
     * @return <code>PassageModuleComplexModelService</code> implementation instance
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PassageModuleComplexModelService getPassageModuleComplexModelService()
            throws JadeApplicationServiceNotAvailableException {
        return (PassageModuleComplexModelService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                PassageModuleComplexModelService.class);
    }

}
