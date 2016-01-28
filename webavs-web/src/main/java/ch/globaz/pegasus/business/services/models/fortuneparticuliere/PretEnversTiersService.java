/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface PretEnversTiersService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PretEnversTiersSearch search) throws PretEnversTiersException, JadePersistenceException;

    /**
     * Permet la création d'une entité pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entité pretEnversTiers à créer
     * @return L'entité pretEnversTiers créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiers create(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entité pretEnversTiers à supprimer
     * @return L'entité pretEnversTiers supprimé
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PretEnversTiers delete(PretEnversTiers pretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité pretEnversTiers
     * 
     * @param idPretEnversTiers
     *            L'identifiant de l'entité pretEnversTiers à charger en mémoire
     * @return L'entité pretEnversTiers chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiers read(String idPretEnversTiers) throws JadePersistenceException, PretEnversTiersException;

    /**
     * Chargement d'un PretEnversTiers via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PretEnversTiersException
     * @throws JadePersistenceException
     */
    public PretEnversTiers readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PretEnversTiersException, JadePersistenceException;

    /**
     * Permet de chercher des pretEnversTiers selon un modèle de critères.
     * 
     * @param pretEnversTiersSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiersSearch search(PretEnversTiersSearch pretEnversTiersSearch) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * 
     * Permet la mise à jour d'une entité pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entité pretEnversTiers à mettre à jour
     * @return L'entité pretEnversTiers mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public PretEnversTiers update(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException;
}
