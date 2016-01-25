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
     * Permet la création d'un revenu
     * 
     * @param simpleRevenu
     *            le revenu à créer
     * @return le revenu crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenu create(SimpleRevenu simpleRevenu) throws JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu
     * 
     * @param simpleRevenu
     * @return le revenu supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenu delete(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un revenu
     * 
     * @param idRevenu
     * @return le revenu
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenu read(String idRevenu) throws RevenuException, JadePersistenceException;

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
    public SimpleRevenuSearch search(SimpleRevenuSearch search) throws JadePersistenceException, RevenuException;

    /**
     * Permet la mise à jour d'un revenu
     * 
     * @param simpleRevenu
     * @return le revenu mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenu update(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
