/**
 * 
 */
package ch.globaz.amal.business.services.models.contribuable;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleContribuableService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'un contribuable
     * 
     * @param simpleContribuable
     *            le contribuable a cr�er
     * @return le contribuable cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleContribuable create(SimpleContribuable simpleContribuable) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet la suppression d'une entit� contribuable
     * 
     * @param contribuable
     *            Le contribuable � supprimer
     * @return Le contribuable supprim�
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleContribuable delete(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un contribuable
     * 
     * @param idContribuable
     *            L'identifiant du contribuable � charger en m�moire
     * @return Le contribuable charg� en m�moire
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleContribuable read(String idContribuable) throws ContribuableException, JadePersistenceException;

    /**
     * Permet la recherche d'une entit� contribuable
     * 
     * @param contribuable
     *            Le contribuable � rechercher
     * @return Les contribuables trouv�s
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleContribuableSearch search(SimpleContribuableSearch search) throws ContribuableException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� contribuable
     * 
     * @param contribuable
     *            Le contribuable � mettre � jour
     * @return Le contribuable mis � jour
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleContribuable update(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException;

}
