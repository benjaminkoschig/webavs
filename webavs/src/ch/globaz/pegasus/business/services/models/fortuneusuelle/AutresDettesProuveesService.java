package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutresDettesProuveesService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AutresDettesProuveesSearch search) throws AutresDettesProuveesException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entit� AutresDettesProuvees � cr�er
     * @return L'entit� AutresDettesProuvees cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutresDettesProuvees create(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entit� AutresDettesProuvees � supprimer
     * @return L'entit� AutresDettesProuvees supprim�
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AutresDettesProuvees delete(AutresDettesProuvees autresDettesProuvees) throws AutresDettesProuveesException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� AutresDettesProuvees
     * 
     * @param idAutresDettesProuvees
     *            L'identifiant de l'entit� AutresDettesProuvees � charger en m�moire
     * @return L'entit� AutresDettesProuvees charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException;

    /**
     * Chargement d'une AutresDettesProuvees via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutresDettesProuveesException
     * @throws JadePersistenceException
     */
    public AutresDettesProuvees readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutresDettesProuveesException, JadePersistenceException;

    /**
     * Permet de chercher des AutresDettesProuvees selon un mod�le de crit�res.
     * 
     * @param AutresDettesProuveesSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutresDettesProuveesSearch search(AutresDettesProuveesSearch autresDettesProuveesSearch)
            throws JadePersistenceException, AutresDettesProuveesException;

    /**
     * 
     * Permet la mise � jour d'une entit� AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entit� AutresDettesProuvees � mettre � jour
     * @return L'entit� AutresDettesProuvees mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutresDettesProuvees update(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException;
}