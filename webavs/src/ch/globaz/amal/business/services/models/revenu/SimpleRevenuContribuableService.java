package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuableSearch;

public interface SimpleRevenuContribuableService extends JadeApplicationService {
    /**
     * Permet la création d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     *            le revenu contribuable à créer
     * @return le revenu contribuable crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuContribuable create(SimpleRevenuContribuable simpleRevenuContribuable)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     * @return le revenu contribuable supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuContribuable delete(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un revenu
     * 
     * @param idRevenu
     * @return le revenu contribuable
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuContribuable read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuContribuableSearch search(SimpleRevenuContribuableSearch search)
            throws JadePersistenceException, RevenuException;

    /**
     * Permet la mise à jour d'un revenu contribuable
     * 
     * @param simpleRevenuContribuable
     * @return le revenu contribuable mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuContribuable update(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
