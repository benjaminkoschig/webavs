/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface BetailService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(BetailSearch search) throws BetailException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � cr�er
     * @return L'entit� b�tail cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Betail create(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � supprimer
     * @return L'entit� b�tail supprim�
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Betail delete(Betail betail) throws BetailException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� betail
     * 
     * @param idBetail
     *            L'identifiant de l'entit� b�tail � charger en m�moire
     * @return L'entit� b�tail charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Betail read(String idBetail) throws JadePersistenceException, BetailException;

    /**
     * Chargement d'un Betail via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BetailException
     * @throws JadePersistenceException
     */
    public Betail readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws BetailException,
            JadePersistenceException;

    /**
     * Permet de chercher des b�tail selon un mod�le de crit�res.
     * 
     * @param betailSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BetailSearch search(BetailSearch betailSearch) throws JadePersistenceException, BetailException;

    /**
     * 
     * Permet la mise � jour d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � mettre � jour
     * @return L'entit� b�tail mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public Betail update(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException;
}
