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
     * Permet la création d'un contribuable
     * 
     * @param simpleContribuable
     *            le contribuable a créer
     * @return le contribuable crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleContribuable create(SimpleContribuable simpleContribuable) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet la suppression d'une entité contribuable
     * 
     * @param contribuable
     *            Le contribuable à supprimer
     * @return Le contribuable supprimé
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleContribuable delete(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un contribuable
     * 
     * @param idContribuable
     *            L'identifiant du contribuable à charger en mémoire
     * @return Le contribuable chargé en mémoire
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleContribuable read(String idContribuable) throws ContribuableException, JadePersistenceException;

    /**
     * Permet la recherche d'une entité contribuable
     * 
     * @param contribuable
     *            Le contribuable à rechercher
     * @return Les contribuables trouvés
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleContribuableSearch search(SimpleContribuableSearch search) throws ContribuableException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité contribuable
     * 
     * @param contribuable
     *            Le contribuable à mettre à jour
     * @return Le contribuable mis à jour
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleContribuable update(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException;

}
