/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.VersionDroitMembreFamilleSearch;

/**
 * @author BSC
 * 
 */
public interface DroitMembreFamilleService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DroitException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public int count(DroitMembreFamilleSearch search) throws DroitException, JadePersistenceException;

    public void delete(DroitMembreFamille droitMembreFamille) throws DroitException, JadePersistenceException;

    /**
     * Permet de supprimer de membre de famaille en fonction d'un id droit.
     * 
     * @param search Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws DroitException Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void deleteByIdDroit(String idDroit) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une droit PC
     * 
     * @param idDroitMembreFamille L'identifiant de la droitMembreFamille à charger en mémoire
     * @return La droitMembreFamille chargée en mémoire
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws DroitException Levée en cas de problème métier dans l'exécution du service
     */
    public DroitMembreFamille read(String idDroitMembreFamille) throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des droits selon un modèle de critères.
     * 
     * @param search Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws DroitException Levée en cas de problème métier dans l'exécution du service
     */
    public DroitMembreFamilleSearch search(DroitMembreFamilleSearch search) throws JadePersistenceException,
            DroitException;

    /**
     * Permet de chercher des droits selon un modèle de critères.
     * 
     * @param search Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws DroitException Levée en cas de problème métier dans l'exécution du service
     */
    public VersionDroitMembreFamilleSearch search(VersionDroitMembreFamilleSearch search)
            throws JadePersistenceException, DroitException;

}
