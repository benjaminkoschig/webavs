package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;

public interface SimpleVersionDroitService extends JadeApplicationService {
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
    public int count(SimpleVersionDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� versionDroit. Le versionDroit doit avoir l'id d'une demande de prestation
     * associ�e qui existe et qui n'est pas d�j� associ�e � un autre versionDroit, sinon une exception est lev�e.
     * 
     * @param versionDroit
     *            Le versionDroit � cr�er
     * @return Le versionDroit cr��
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleVersionDroit create(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� versionDroit
     * 
     * @param versionDroit
     *            Le versionDroit � supprimer
     * @return Le versionDroit supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleVersionDroit delete(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idVersionDroit
     *            L'identifiant du versionDroit � charger en m�moire
     * @return Le versionDroit charg� en m�moire
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleVersionDroit read(String idVersionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des simpleVersionDroit selon un mod�le de crit�res.
     * 
     * @param SimpleVersionDroitSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleVersionDroitSearch search(SimpleVersionDroitSearch search) throws JadePersistenceException,
            DroitException;

    /**
     * Permet la mise � jour d'une entit� versionDroit
     * 
     * @param versionDroit
     *            Le versionDroit � mettre � jour
     * @return Le versionDroit mis � jour
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleVersionDroit update(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

}
