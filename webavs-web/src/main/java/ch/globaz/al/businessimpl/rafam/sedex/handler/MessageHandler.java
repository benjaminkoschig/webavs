package ch.globaz.al.businessimpl.rafam.sedex.handler;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Map;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

/**
 * G�re l'importation des annonces de retour de la centrale
 * 
 * @author jts
 * 
 */
public interface MessageHandler {

    /**
     * Ex�cute le traitement appropri� au type d'annonce en cours de traitement
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @return l'annonce mise � jour par le traitement (importation)
     */
    // TODO javadoc param�tre
    public AnnonceRafamModel traiterMessage(Map<String, Object> params) throws JadeApplicationException,
            JadePersistenceException;

}
