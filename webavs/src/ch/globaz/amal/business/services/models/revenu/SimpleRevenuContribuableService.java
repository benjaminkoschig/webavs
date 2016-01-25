package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuableSearch;

public interface SimpleRevenuContribuableService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     *            le revenu contribuable � cr�er
     * @return le revenu contribuable cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuContribuable create(SimpleRevenuContribuable simpleRevenuContribuable)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     * @return le revenu contribuable supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuContribuable delete(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un revenu
     * 
     * @param idRevenu
     * @return le revenu contribuable
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuContribuable read(String idRevenu) throws RevenuException, JadePersistenceException;

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
    public SimpleRevenuContribuableSearch search(SimpleRevenuContribuableSearch search)
            throws JadePersistenceException, RevenuException;

    /**
     * Permet la mise � jour d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     * @return le revenu contribuable mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuContribuable update(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
