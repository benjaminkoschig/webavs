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
     * Permet la création d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     *            le revenu sourcier à créer
     * @return le revenu sourcier crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuSourcier create(SimpleRevenuSourcier simpleRevenuSourcier) throws JadePersistenceException,
            RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     * @return le revenu sourcier supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleRevenuSourcier delete(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire un revenu
     * 
     * @param idRevenu
     * @return le revenu sourcier
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuSourcier read(String idRevenu) throws RevenuException, JadePersistenceException;

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
    public SimpleRevenuSourcierSearch search(SimpleRevenuSourcierSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise à jour d'un revenu sourcier
     * 
     * @param simpleRevenuSourcier
     * @return le revenu sourcier mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuSourcier update(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
