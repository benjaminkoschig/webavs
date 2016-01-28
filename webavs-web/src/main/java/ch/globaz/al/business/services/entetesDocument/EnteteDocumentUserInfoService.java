package ch.globaz.al.business.services.entetesDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service sp�cifique permettant de r�cup�rer les infos relatives � l'utilisateur
 * 
 * @author PTA
 * 
 */

public interface EnteteDocumentUserInfoService extends JadeApplicationService {

    /**
     * M�thode qui ajoute les diff�rentes de l'utilisateur au document
     * 
     * @param document
     *            document auxquel il faut ajouter les infos de l'utilisateur
     * @param infoUser
     *            info user stock� dans une hashmap cl� et valeur (mail, phone, ...)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void addUserInfoToDocument(DocumentData document, HashMap<String, String> infoUser)
            throws JadeApplicationException, JadePersistenceException;

}
