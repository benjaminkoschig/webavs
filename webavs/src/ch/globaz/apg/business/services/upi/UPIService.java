/**
 * 
 */
package ch.globaz.apg.business.services.upi;

import globaz.apg.pojo.Person;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * @author dde
 * 
 */
public interface UPIService extends JadeApplicationService {

    /**
     * Retourne les informations UPI d'une personne
     * 
     * @param nss
     *            Le NSS de la personne recherch�
     * @return Person
     */
    public Person getPerson(String nss) throws Exception;

}
