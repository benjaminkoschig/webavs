/**
 * 
 */
package ch.globaz.amal.business.services.models.controleurEnvoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;

/**
 * @author DHI
 * 
 */
public interface SimpleControleurJobService extends JadeApplicationService {
    /**
     * Permet la création d'un job en db
     * 
     * @param simpleControleurJob
     *            le job à créer
     * @return le job créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob create(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException;

    /**
     * Permet la suppression d'un job
     * 
     * @param simpleControleurJob
     * @return le job supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public SimpleControleurJob delete(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, ControleurJobException;

    /**
     * Permet de charger en mémoire un job
     * 
     * @param idJob
     * @return le job
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob read(String idJob) throws JadePersistenceException, ControleurJobException;

    /**
     * Permet la recherche d'un job
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJobSearch search(SimpleControleurJobSearch search) throws JadePersistenceException,
            ControleurJobException;

    /**
     * Permet la mise à jour d'un job
     * 
     * @param simpleControleurJob
     * @return le job mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob update(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException;

}
