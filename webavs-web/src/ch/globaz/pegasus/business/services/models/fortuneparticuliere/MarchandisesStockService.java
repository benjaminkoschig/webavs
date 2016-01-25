/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface MarchandisesStockService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(MarchandisesStockSearch search) throws MarchandisesStockException, JadePersistenceException;

    /**
     * Permet la création d'une entité marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entité marchandisesStock à créer
     * @return L'entité marchandisesStock créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MarchandisesStock create(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entité marchandisesStock à supprimer
     * @return L'entité marchandisesStock supprimé
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public MarchandisesStock delete(MarchandisesStock marchandisesStock) throws MarchandisesStockException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité marchandisesStock
     * 
     * @param idMarchandisesStock
     *            L'identifiant de l'entité marchandisesStock à charger en mémoire
     * @return L'entité marchandisesStock chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException;

    /**
     * Chargement d'une MarchandisesStoc via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws MarchandisesStocException
     * @throws JadePersistenceException
     */
    public MarchandisesStock readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws MarchandisesStockException, JadePersistenceException;

    /**
     * Permet de chercher des marchandisesStock selon un modèle de critères.
     * 
     * @param marchandisesStockSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MarchandisesStockSearch search(MarchandisesStockSearch marchandisesStockSearch)
            throws JadePersistenceException, MarchandisesStockException;

    /**
     * 
     * Permet la mise à jour d'une entité marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entité marchandisesStock à mettre à jour
     * @return L'entité marchandisesStock mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public MarchandisesStock update(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException;
}
