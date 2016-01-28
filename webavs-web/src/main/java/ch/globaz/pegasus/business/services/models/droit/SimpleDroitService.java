package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;

public interface SimpleDroitService extends JadeApplicationService {

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
    public int count(SimpleDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� droit. Le droit doit avoir l'id d'une demande de prestation associ�e qui existe
     * et qui n'est pas d�j� associ�e � un autre droit, sinon une exception est lev�e.
     * 
     * @param droit
     *            Le droit � cr�er
     * @return Le droit cr��
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroit create(SimpleDroit droit) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� droit
     * 
     * @param droit
     *            Le droit � supprimer
     * @return Le droit supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroit delete(SimpleDroit droit) throws DroitException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un droit
     * 
     * @param idDroit
     *            L'identifiant du droit � charger en m�moire
     * @return Le droit charg� en m�moire
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroit read(String idDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet d'effectuer une recher
     * 
     * @param search
     * @return
     * @throws DroitException
     * @throws JadePersistenceException
     */
    public SimpleDroitSearch search(SimpleDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� droit
     * 
     * @param droit
     *            Le droit � mettre � jour
     * @return Le droit mis � jour
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDroit update(SimpleDroit droit) throws DroitException, JadePersistenceException;

}
