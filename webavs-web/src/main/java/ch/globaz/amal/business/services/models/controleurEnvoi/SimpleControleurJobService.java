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
     * Permet la cr�ation d'un job en db
     * 
     * @param simpleControleurJob
     *            le job � cr�er
     * @return le job cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob create(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException;

    /**
     * Permet la suppression d'un job
     * 
     * @param simpleControleurJob
     * @return le job supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public SimpleControleurJob delete(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, ControleurJobException;

    /**
     * Permet de charger en m�moire un job
     * 
     * @param idJob
     * @return le job
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob read(String idJob) throws JadePersistenceException, ControleurJobException;

    /**
     * Permet la recherche d'un job
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJobSearch search(SimpleControleurJobSearch search) throws JadePersistenceException,
            ControleurJobException;

    /**
     * Permet la mise � jour d'un job
     * 
     * @param simpleControleurJob
     * @return le job mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ControleurJobException
     */
    public SimpleControleurJob update(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException;

}
