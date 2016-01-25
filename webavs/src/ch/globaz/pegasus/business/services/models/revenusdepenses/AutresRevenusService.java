package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenusSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutresRevenusService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AutresRevenusSearch search) throws AutresRevenusException, JadePersistenceException;

    /**
     * Permet la création d'une entité AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entité AutresRevenus à créer
     * @return L'entité AutresRevenus créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenus create(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entité AutresRevenus à supprimer
     * @return L'entité AutresRevenus supprimé
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AutresRevenus delete(AutresRevenus autresRevenus) throws AutresRevenusException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité AutresRevenus
     * 
     * @param idAutresRevenus
     *            L'identifiant de l'entité AutresRevenus à charger en mémoire
     * @return L'entité AutresRevenus chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenus read(String idAutresRevenus) throws JadePersistenceException, AutresRevenusException;

    /**
     * Chargement d'un AutresRevenus via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutresRevenusException
     * @throws JadePersistenceException
     */
    public AutresRevenus readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutresRevenusException,
            JadePersistenceException;

    /**
     * Permet de chercher des AutresRevenus selon un modèle de critères.
     * 
     * @param AutresRevenusSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenusSearch search(AutresRevenusSearch autresRevenusSearch) throws JadePersistenceException,
            AutresRevenusException;

    /**
     * 
     * Permet la mise à jour d'une entité AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entité AutresRevenus à mettre à jour
     * @return L'entité AutresRevenus mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutresRevenus update(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException;
}