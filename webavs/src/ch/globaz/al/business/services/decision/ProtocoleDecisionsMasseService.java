package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

public interface ProtocoleDecisionsMasseService extends JadeApplicationService {
    /**
     * Génère la structure de données du protocole d'erreur d'impression de masse de décisions
     * 
     * @param logger
     *            Instance du logger contenant les messages à afficher sur le protocole
     * @param params
     *            Paramètres tels que le numéro de passage, labels du processus et du traitement et la période
     * @return Un DocumentDataContainer contenant le document et le logger passé en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentDataContainer getDocumentData(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException;
}
