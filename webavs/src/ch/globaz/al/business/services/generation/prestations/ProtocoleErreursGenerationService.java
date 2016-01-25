package ch.globaz.al.business.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service de g�n�ration de protocole d'erreurs de g�n�ration de prestations
 * 
 * @author jts
 * 
 */
public interface ProtocoleErreursGenerationService extends JadeApplicationService {

    /**
     * G�n�re la structure de donn�es du protocole d'erreur de g�n�ration
     * 
     * @param logger
     *            Instance du logger contenant les messages � afficher sur le protocole
     * @param params
     *            Param�tres tels que le num�ro de passage, labels du processus et du traitement et la p�riode
     * @return Le document g�n�r�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException;
}
