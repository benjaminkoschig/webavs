/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface BetailService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(BetailSearch search) throws BetailException, JadePersistenceException;

    /**
     * Permet la création d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à créer
     * @return L'entité bétail créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Betail create(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à supprimer
     * @return L'entité bétail supprimé
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Betail delete(Betail betail) throws BetailException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité betail
     * 
     * @param idBetail
     *            L'identifiant de l'entité bétail à charger en mémoire
     * @return L'entité bétail chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Betail read(String idBetail) throws JadePersistenceException, BetailException;

    /**
     * Chargement d'un Betail via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BetailException
     * @throws JadePersistenceException
     */
    public Betail readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws BetailException,
            JadePersistenceException;

    /**
     * Permet de chercher des bétail selon un modèle de critères.
     * 
     * @param betailSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BetailSearch search(BetailSearch betailSearch) throws JadePersistenceException, BetailException;

    /**
     * 
     * Permet la mise à jour d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à mettre à jour
     * @return L'entité bétail mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public Betail update(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException;
}
