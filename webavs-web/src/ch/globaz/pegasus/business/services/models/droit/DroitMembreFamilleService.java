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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DroitException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DroitMembreFamilleSearch search) throws DroitException, JadePersistenceException;

    public void delete(DroitMembreFamille droitMembreFamille) throws DroitException, JadePersistenceException;

    /**
     * Permet de supprimer de membre de famaille en fonction d'un id droit.
     * 
     * @param search Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void deleteByIdDroit(String idDroit) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une droit PC
     * 
     * @param idDroitMembreFamille L'identifiant de la droitMembreFamille � charger en m�moire
     * @return La droitMembreFamille charg�e en m�moire
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DroitMembreFamille read(String idDroitMembreFamille) throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des droits selon un mod�le de crit�res.
     * 
     * @param search Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DroitMembreFamilleSearch search(DroitMembreFamilleSearch search) throws JadePersistenceException,
            DroitException;

    /**
     * Permet de chercher des droits selon un mod�le de crit�res.
     * 
     * @param search Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public VersionDroitMembreFamilleSearch search(VersionDroitMembreFamilleSearch search)
            throws JadePersistenceException, DroitException;

}
