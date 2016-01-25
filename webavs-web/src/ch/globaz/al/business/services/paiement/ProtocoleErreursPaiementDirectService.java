package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Service de g�n�ration de protocole d'erreurs de compensation de prestations
 * 
 * @author jts
 * 
 */
public interface ProtocoleErreursPaiementDirectService extends JadeApplicationService {

    /**
     * G�n�re la structure de donn�es du protocole d'erreur de paiement direct
     * 
     * @param logger
     *            Instance du logger contenant les messages � afficher sur le protocole
     * @param params
     *            Param�tres tels que le num�ro de passage, labels du processus et du traitement et la p�riode
     * @return Un DocumentDataContainer contenant le document et le logger pass� en param�tre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentDataContainer getDocumentData(ProtocoleLogger logger, HashMap params)
            throws JadePersistenceException, JadeApplicationException;
}
