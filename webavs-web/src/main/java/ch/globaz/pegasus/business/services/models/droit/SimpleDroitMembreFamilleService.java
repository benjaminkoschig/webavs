package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamilleSearch;

public interface SimpleDroitMembreFamilleService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDroitMembreFamilleSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� droitMembreFamille.
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille � cr�er
     * @return Le droitMembreFamille cr��
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroitMembreFamille create(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� droitMembreFamille
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille � supprimer
     * @return Le droitMembreFamille supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroitMembreFamille delete(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un droitMembreFamille
     * 
     * @param idDroitMembreFamille
     *            L'identifiant du droitMembreFamille � charger en m�moire
     * @return Le droitMembreFamille charg� en m�moire
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroitMembreFamille read(String idDroitMembreFamille) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des droitMembreFamille selon un mod�le de crit�res.
     * 
     * @param droitMembreFamilleSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDroitMembreFamilleSearch search(SimpleDroitMembreFamilleSearch droitMembreFamilleSearch)
            throws JadePersistenceException, DroitException;

    /**
     * Permet la mise � jour d'une entit� droitMembreFamille
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille � mettre � jour
     * @return Le droitMembreFamille mis � jour
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroitMembreFamille update(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

}
