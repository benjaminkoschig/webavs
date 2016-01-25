/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface DessaisissementRevenuService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DessaisissementRevenuSearch search) throws DessaisissementRevenuException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � cr�er
     * @return L'entit� b�tail cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementRevenu create(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � supprimer
     * @return L'entit� b�tail supprim�
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DessaisissementRevenu delete(DessaisissementRevenu betail) throws DessaisissementRevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� betail
     * 
     * @param idDessaisissementRevenu
     *            L'identifiant de l'entit� b�tail � charger en m�moire
     * @return L'entit� b�tail charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException;

    /**
     * Chargement d'une DessaisissementRevenu via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementRevenuSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public DessaisissementRevenu readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementRevenuException, JadePersistenceException;

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException;

    /**
     * Permet de chercher des b�tail selon un mod�le de crit�res.
     * 
     * @param betailSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementRevenuSearch search(DessaisissementRevenuSearch betailSearch)
            throws JadePersistenceException, DessaisissementRevenuException;

    /**
     * 
     * Permet la mise � jour d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � mettre � jour
     * @return L'entit� b�tail mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public DessaisissementRevenu update(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException;
}
