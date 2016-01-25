/**
 * 
 */
package ch.globaz.amal.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.services.interapplication.PCCustomerService;

/**
 * Localisateur des services Amal Inter application (usage à destination des autres applications)
 * 
 * @author dhi
 * 
 */
public class AmalInterApplicationServiceLocator {

    /**
     * Récupération d'une instance du service PCCustomer (usage PC)
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PCCustomerService getPCCustomerService() throws JadeApplicationServiceNotAvailableException {
        return (PCCustomerService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PCCustomerService.class);
    }

}
