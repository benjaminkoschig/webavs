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
     *                          Le NSS de la personne recherché
     * @param langue
     * @param userEmail
     * @param loginName
     * @param numeroAffilie
     * @return Person
     */
    public InfosPersonResponseType getPerson(String nss, String numeroAffilie, String loginName, String userEmail,
            String langue) throws Exception;

}
