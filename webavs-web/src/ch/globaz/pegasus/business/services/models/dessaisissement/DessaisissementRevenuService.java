/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface DessaisissementRevenuService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DessaisissementRevenuSearch search) throws DessaisissementRevenuException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à créer
     * @return L'entité bétail créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementRevenu create(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à supprimer
     * @return L'entité bétail supprimé
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DessaisissementRevenu delete(DessaisissementRevenu betail) throws DessaisissementRevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité betail
     * 
     * @param idDessaisissementRevenu
     *            L'identifiant de l'entité bétail à charger en mémoire
     * @return L'entité bétail chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException;

    /**
     * Chargement d'une DessaisissementRevenu via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementRevenuSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public DessaisissementRevenu readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementRevenuException, JadePersistenceException;

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException;

    /**
     * Permet de chercher des bétail selon un modèle de critères.
     * 
     * @param betailSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementRevenuSearch search(DessaisissementRevenuSearch betailSearch)
            throws JadePersistenceException, DessaisissementRevenuException;

    /**
     * 
     * Permet la mise à jour d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à mettre à jour
     * @return L'entité bétail mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public DessaisissementRevenu update(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException;
}
