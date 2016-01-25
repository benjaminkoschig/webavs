package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service de g�n�ration de protocole d'informattion de d�claration de versement
 * 
 * @author PTA
 * 
 */
public interface ProtocoleInfoDeclarationVersementService extends JadeApplicationService {

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
