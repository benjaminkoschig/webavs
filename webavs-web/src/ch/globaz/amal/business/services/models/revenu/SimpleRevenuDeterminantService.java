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
     * Permet la création d'un revenu déterminant
     * 
     * @param simpleRevenuDéterminant
     *            le revenu à créer
     * @return le revenu crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuDeterminant create(SimpleRevenuDeterminant simpleRevenuDeterminant)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu determinant
     * 
     * @param simpleRevenuDeterminant
     * @return le revenu supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuDeterminant delete(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un revenu déterminant
     * 
     * @param idRevenu
     * @return le revenu déterminant
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuDeterminant read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple déterminant
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuDeterminantSearch search(SimpleRevenuDeterminantSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise à jour d'un revenu déterminant
     * 
     * @param simpleRevenuHistorique
     * @return le revenu mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuDeterminant update(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
