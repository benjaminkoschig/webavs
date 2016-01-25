/**
 * 
 */
package ch.globaz.amal.business.services.models.document;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;

/**
 * @author DHI
 * 
 */
public interface SimpleDocumentService extends JadeApplicationService {
    /**
     * Permet la création d'un document
     * 
     * @param simpleDocument
     *            le document à créer
     * @return le document créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     * @throws DetailFamilleException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public SimpleDocument create(SimpleDocument simpleDocument) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException,
            ControleurEnvoiException, AnnonceException, ControleurJobException;

    /**
     * Permet la création d'un document, avec paramètre du type de job
     * 
     * @param simpleDocument
     *            le document à créer
     * @param csJobType
     *            le code système du type de job
     * @return le document créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     * @throws DetailFamilleException
     * @throws AnnonceException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public SimpleDocument create(SimpleDocument simpleDocument, String csJobType) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException,
            ControleurEnvoiException, AnnonceException, ControleurJobException;

    /**
     * Permet la suppression d'un document
     * 
     * @param simpleDocument
     * @return le document supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     */
    public SimpleDocument delete(SimpleDocument simpleDocument) throws JadePersistenceException, DocumentException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException;

    /**
     * Permet de charger en mémoire un document
     * 
     * @param idDocument
     * @return le document
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     */
    public SimpleDocument read(String idDocument) throws JadePersistenceException, DocumentException;

    /**
     * Permet la recherche d'un document
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     */
    public SimpleDocumentSearch search(SimpleDocumentSearch search) throws JadePersistenceException, DocumentException;

    /**
     * Permet la mise à jour d'un document
     * 
     * @param simpleDocument
     * @return le document mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DocumentException
     * @throws DetailFamilleException
     */
    public SimpleDocument update(SimpleDocument simpleDocument) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException;

}
