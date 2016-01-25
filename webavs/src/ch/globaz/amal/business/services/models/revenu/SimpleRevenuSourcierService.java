/**
 * 
 */
package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcierSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleRevenuSourcierService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     *            le revenu sourcier � cr�er
     * @return le revenu sourcier cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuSourcier create(SimpleRevenuSourcier simpleRevenuSourcier) throws JadePersistenceException,
            RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     * @return le revenu sourcier supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleRevenuSourcier delete(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire un revenu
     * 
     * @param idRevenu
     * @return le revenu sourcier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuSourcier read(String idRevenu) throws RevenuException, JadePersistenceException;

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
    public SimpleRevenuSourcierSearch search(SimpleRevenuSourcierSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise � jour d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     * @return le revenu sourcier mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuSourcier update(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
