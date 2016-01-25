package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

/**
 * Gère l'importation des annonces de retour de la centrale
 * 
 * @author jts
 * 
 */
public interface MessageHandler {

    /**
     * Exécute le traitement approprié au type d'annonce en cours de traitement
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @return l'annonce mise à jour par le traitement (importation)
     */
    // TODO javadoc paramètre
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException;

}
