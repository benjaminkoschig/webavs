/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.VehiculeSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface VehiculeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(VehiculeSearch search) throws VehiculeException, JadePersistenceException;

    /**
     * Permet la création d'une entité véhicule
     * 
     * @param vehicule
     *            L'entité véhicule à créer
     * @return L'entité véhicule créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Vehicule create(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité véhicule
     * 
     * @param vehicule
     *            L'entité véhicule à supprimer
     * @return L'entité véhicule supprimé
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Vehicule delete(Vehicule vehicule) throws VehiculeException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité véhicule
     * 
     * @param idVehicule
     *            L'identifiant de l'entité véhicule à charger en mémoire
     * @return L'entité véhicule chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Vehicule read(String idVehicule) throws JadePersistenceException, VehiculeException;

    /**
     * Chargement d'une Vehicule via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws VehiculeException
     * @throws JadePersistenceException
     */
    public Vehicule readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws VehiculeException,
            JadePersistenceException;

    /**
     * Permet de chercher des véhicules selon un modèle de critères.
     * 
     * @param vehiculeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public VehiculeSearch search(VehiculeSearch vehiculeSearch) throws JadePersistenceException, VehiculeException;

    /**
     * 
     * Permet la mise à jour d'une entité véhicule
     * 
     * @param vehicule
     *            L'entité véhicule à mettre à jour
     * @return L'entité véhicule mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public Vehicule update(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException;
}
