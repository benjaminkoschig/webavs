/**
 * 
 */
package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminantSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleRevenuDeterminantService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'un revenu d�terminant
     * 
     * @param simpleRevenuD�terminant
     *            le revenu � cr�er
     * @return le revenu cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuDeterminant create(SimpleRevenuDeterminant simpleRevenuDeterminant)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu determinant
     * 
     * @param simpleRevenuDeterminant
     * @return le revenu supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuDeterminant delete(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un revenu d�terminant
     * 
     * @param idRevenu
     * @return le revenu d�terminant
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuDeterminant read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple d�terminant
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuDeterminantSearch search(SimpleRevenuDeterminantSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise � jour d'un revenu d�terminant
     * 
     * @param simpleRevenuHistorique
     * @return le revenu mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuDeterminant update(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
