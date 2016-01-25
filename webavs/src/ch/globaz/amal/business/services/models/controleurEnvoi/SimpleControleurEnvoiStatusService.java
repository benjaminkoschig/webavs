/**
 * 
 */
package ch.globaz.amal.business.services.models.controleurEnvoi;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;

/**
 * @author DHI
 * 
 */
public interface SimpleControleurEnvoiStatusService extends JadeApplicationService {

    /**
     * Permet le changement d'un status d'envoi (ligne d'un job avec nouveau status)
     * 
     * @param idStatus
     *            l'id du status
     * @param newStatus
     *            le nouveau status
     * @param generateGlobal
     *            génération du status du job
     * @param jobError
     *            Message d'erreur du job
     * @return le status créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws AnnonceException
     * @throws ControleurJobException
     */
    public SimpleControleurEnvoiStatus changeStatus(String idStatus, String newStatus, Boolean generateGlobal,
            String jobError) throws JadePersistenceException, JadeApplicationServiceNotAvailableException,
            ControleurEnvoiException, AnnonceException, DocumentException, ControleurJobException;

    /**
     * 
     * Retourne le nombre d'élément selon le modèle de recherche
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws ControleurEnvoiException
     */
    public int count(SimpleControleurEnvoiStatusSearch search) throws JadePersistenceException,
            ControleurEnvoiException;

    /**
     * Permet la création d'un status d'envoi (ligne d'un job)
     * 
     * @param simpleControleurEnvoiStatus
     *            le status à créé
     * @return le status créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws AnnonceException
     * @throws ControleurJobException
     */
    public SimpleControleurEnvoiStatus create(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, ControleurEnvoiException,
            AnnonceException, DocumentException, ControleurJobException;

    /**
     * Permet la suppression d'un status d'envoi
     * 
     * @param simpleControleurEnvoiStatus
     * @return le status supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     */
    public SimpleControleurEnvoiStatus delete(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, ControleurEnvoiException;

    /**
     * Permet la suppression d'un status d'envoi
     * 
     * @param idStatus
     * @return le status supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     */
    public SimpleControleurEnvoiStatus delete(String idStatus) throws JadePersistenceException,
            ControleurEnvoiException;

    /**
     * Récupération du libellé du code user correspondant, pour les documents
     * 
     * @param csCodeUser
     * @param session
     * @return 1 - DECBAL - blablablabla
     */
    public String getDocumentLibelle(String csCodeUser, BSession session);

    /**
     * Permet de charger en mémoire un status d'envoi
     * 
     * @param idStatus
     * @return le status
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleControleurEnvoiStatus read(String idStatus) throws JadePersistenceException;

    /**
     * Permet la recherche d'un status
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     */
    public SimpleControleurEnvoiStatusSearch search(SimpleControleurEnvoiStatusSearch search)
            throws JadePersistenceException, ControleurEnvoiException;

    /**
     * Permet la mise à jour d'un job
     * 
     * @param simpleControleurJob
     * @return le job mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws AnnonceException
     * @throws ControleurJobException
     */
    public SimpleControleurEnvoiStatus update(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, ControleurEnvoiException,
            AnnonceException, DocumentException, ControleurJobException;

}
