package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;

public interface SimpleHomeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleHomeSearch search) throws HomeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� home
     * 
     * @param home
     *            Le home � cr�er
     * @return Le home cr��
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleHome create(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� home
     * 
     * @param home
     *            Le home � supprimer
     * @return Le home supprim�
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleHome delete(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un home
     * 
     * @param idDossier
     *            L'identifiant du home � charger en m�moire
     * @return Le home charg� en m�moire
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleHome read(String idHome) throws HomeException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� home
     * 
     * @param home
     *            Le home � mettre � jour
     * @return Le home mis � jour
     * @throws HomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleHome update(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * 
     * @param search le mod�le rechercher
     * @return le r�sultat de la requete
     * @throws HomeException si probl�me "m�tier"
     * @throws JadePersistenceException si probl�me "technique"
     */
    public SimpleHomeSearch search(SimpleHomeSearch search) throws HomeException, JadePersistenceException;
}
