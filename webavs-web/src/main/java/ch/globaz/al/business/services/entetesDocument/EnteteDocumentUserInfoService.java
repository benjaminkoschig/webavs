package ch.globaz.al.business.services.entetesDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service spécifique permettant de récupérer les infos relatives à l'utilisateur
 * 
 * @author PTA
 * 
 */

public interface EnteteDocumentUserInfoService extends JadeApplicationService {

    /**
     * Méthode qui ajoute les différentes de l'utilisateur au document
     * 
     * @param document
     *            document auxquel il faut ajouter les infos de l'utilisateur
     * @param infoUser
     *            info user stocké dans une hashmap clé et valeur (mail, phone, ...)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void addUserInfoToDocument(DocumentData document, HashMap<String, String> infoUser)
            throws JadeApplicationException, JadePersistenceException;

}
