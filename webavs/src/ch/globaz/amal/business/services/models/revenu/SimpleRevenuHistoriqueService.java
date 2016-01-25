/**
 * 
 */
package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleRevenuHistoriqueService extends JadeApplicationService {
    /**
     * Permet la création d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     *            le revenu à créer
     * @return le revenu crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHistorique create(SimpleRevenuHistorique simpleRevenuHistorique)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     * @return le revenu supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHistorique delete(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un revenu historique
     * 
     * @param idRevenu
     * @return le revenu historique
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHistorique read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple historique
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHistoriqueSearch search(SimpleRevenuHistoriqueSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise à jour d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     * @return le revenu mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuHistorique update(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
