package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service de g�n�ration du protocole d'erreurs de l'impression des avis d'�ch�ances
 * 
 * @author PTA
 * 
 */
public interface ProtocoleErreursImpressionEcheancesService extends JadeApplicationService {

    /**
     * M�thode g�n�rant la structure du document
     * 
     * @param logger
     *            ProtocoleLogger
     * @param params
     *            liste d'infos pour destin�s au protocole
     * 
     * @return DocumentData document: protocole technique
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap params) throws JadePersistenceException,
            JadeApplicationException;

}
