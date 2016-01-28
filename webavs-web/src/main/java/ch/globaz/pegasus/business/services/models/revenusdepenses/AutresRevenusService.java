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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AutresRevenusSearch search) throws AutresRevenusException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entit� AutresRevenus � cr�er
     * @return L'entit� AutresRevenus cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutresRevenus create(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entit� AutresRevenus � supprimer
     * @return L'entit� AutresRevenus supprim�
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AutresRevenus delete(AutresRevenus autresRevenus) throws AutresRevenusException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� AutresRevenus
     * 
     * @param idAutresRevenus
     *            L'identifiant de l'entit� AutresRevenus � charger en m�moire
     * @return L'entit� AutresRevenus charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     * Permet de chercher des AutresRevenus selon un mod�le de crit�res.
     * 
     * @param AutresRevenusSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutresRevenusSearch search(AutresRevenusSearch autresRevenusSearch) throws JadePersistenceException,
            AutresRevenusException;

    /**
     * 
     * Permet la mise � jour d'une entit� AutresRevenus
     * 
     * @param AutresRevenus
     *            L'entit� AutresRevenus � mettre � jour
     * @return L'entit� AutresRevenus mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutresRevenus update(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException;
}