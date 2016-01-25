package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service de génération de protocole d'informattion de déclaration de versement
 * 
 * @author PTA
 * 
 */
public interface ProtocoleInfoDeclarationVersementService extends JadeApplicationService {

    /**
     * Méthode générant la structure du document
     * 
     * @param logger
     *            ProtocoleLogger
     * @param params
     *            liste d'infos pour destinés au protocole
     * 
     * @return DocumentData document: protocole technique
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap params) throws JadePersistenceException,
            JadeApplicationException;

}
