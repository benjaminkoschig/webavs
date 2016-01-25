package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface CotisationsPsalService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(CotisationsPsalSearch search) throws CotisationsPsalException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entit� CotisationsPsal � cr�er
     * @return L'entit� CotisationsPsal cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CotisationsPsal create(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entit� CotisationsPsal � supprimer
     * @return L'entit� CotisationsPsal supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CotisationsPsal delete(CotisationsPsal cotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� CotisationsPsal
     * 
     * @param idCotisationsPsal
     *            L'identifiant de l'entit� CotisationsPsal � charger en m�moire
     * @return L'entit� CotisationsPsal charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException, CotisationsPsalException;

    /**
     * Chargement d'un CotisationsPsal via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     */
    public CotisationsPsal readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws CotisationsPsalException, JadePersistenceException;

    /**
     * Permet de chercher des CotisationsPsal selon un mod�le de crit�res.
     * 
     * @param CotisationsPsalSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CotisationsPsalSearch search(CotisationsPsalSearch cotisationsPsalSearch) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * 
     * Permet la mise � jour d'une entit� CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entit� CotisationsPsal � mettre � jour
     * @return L'entit� CotisationsPsal mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public CotisationsPsal update(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException;
}