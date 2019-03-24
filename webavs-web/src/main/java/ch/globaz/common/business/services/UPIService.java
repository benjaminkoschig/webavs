/**
 *
 */
package ch.globaz.common.business.services;

import ch.globaz.common.business.models.InfosPersonResponseType;
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
     *                Le NSS de la personne recherché
     * @return Person
     */
    public InfosPersonResponseType getPerson(String nss) throws Exception;

}
