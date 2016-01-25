/**
 * 
 */
package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleRevenuService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'un revenu
     * 
     * @param simpleRevenu
     *            le revenu � cr�er
     * @return le revenu cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenu create(SimpleRevenu simpleRevenu) throws JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu
     * 
     * @param simpleRevenu
     * @return le revenu supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenu delete(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un revenu
     * 
     * @param idRevenu
     * @return le revenu
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenu read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuSearch search(SimpleRevenuSearch search) throws JadePersistenceException, RevenuException;

    /**
     * Permet la mise � jour d'un revenu
     * 
     * @param simpleRevenu
     * @return le revenu mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenu update(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
