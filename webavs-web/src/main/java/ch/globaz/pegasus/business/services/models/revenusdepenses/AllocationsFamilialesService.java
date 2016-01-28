package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AllocationsFamilialesService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AllocationsFamilialesSearch search) throws AllocationsFamilialesException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entité AllocationsFamiliales à créer
     * @return L'entité AllocationsFamiliales créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamiliales create(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entité AllocationsFamiliales à supprimer
     * @return L'entité AllocationsFamiliales supprimé
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AllocationsFamiliales delete(AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité AllocationsFamiliales
     * 
     * @param idAllocationsFamiliales
     *            L'identifiant de l'entité AllocationsFamiliales à charger en mémoire
     * @return L'entité AllocationsFamiliales chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

    /**
     * Chargement d'une AllocationsFamiliales via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AllocationsFamilialesException
     * @throws JadePersistenceException
     */
    public AllocationsFamiliales readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AllocationsFamilialesException, JadePersistenceException;

    /**
     * Permet de chercher des AllocationsFamiliales selon un modèle de critères.
     * 
     * @param AllocationsFamilialesSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamilialesSearch search(AllocationsFamilialesSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException;

    /**
     * 
     * Permet la mise à jour d'une entité AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entité AllocationsFamiliales à mettre à jour
     * @return L'entité AllocationsFamiliales mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AllocationsFamiliales update(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException;
}