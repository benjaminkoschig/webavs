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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(VehiculeSearch search) throws VehiculeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� v�hicule
     * 
     * @param vehicule
     *            L'entit� v�hicule � cr�er
     * @return L'entit� v�hicule cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Vehicule create(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� v�hicule
     * 
     * @param vehicule
     *            L'entit� v�hicule � supprimer
     * @return L'entit� v�hicule supprim�
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Vehicule delete(Vehicule vehicule) throws VehiculeException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� v�hicule
     * 
     * @param idVehicule
     *            L'identifiant de l'entit� v�hicule � charger en m�moire
     * @return L'entit� v�hicule charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     * Permet de chercher des v�hicules selon un mod�le de crit�res.
     * 
     * @param vehiculeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public VehiculeSearch search(VehiculeSearch vehiculeSearch) throws JadePersistenceException, VehiculeException;

    /**
     * 
     * Permet la mise � jour d'une entit� v�hicule
     * 
     * @param vehicule
     *            L'entit� v�hicule � mettre � jour
     * @return L'entit� v�hicule mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public Vehicule update(Vehicule vehicule) throws JadePersistenceException, VehiculeException,
            DonneeFinanciereException;
}
