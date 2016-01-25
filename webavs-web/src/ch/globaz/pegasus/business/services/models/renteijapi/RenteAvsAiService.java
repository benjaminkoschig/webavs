package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RenteAvsAiService extends JadeApplicationService, AbstractDonneeFinanciereService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RenteAvsAiSearch search) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param renteAvsAi
     *            La renteAvsAi � cr�er
     * @return La renteAvsAi cr��
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAvsAi create(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            La renteAvsAi � supprimer
     * @return La renteAvsAi supprim�
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAvsAi delete(RenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idrenteAvsAi
     *            L'identifiant de la renteAvsAie � charger en m�moire
     * @return La renteAvsAi charg� en m�moire
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAvsAi read(String idRenteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Chargement d'une rente avs ai via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     */
    public RenteAvsAi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws RenteAvsAiException,
            JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param renteAvsAiSearch
     * @return La recherche effectu�
     * @throws RenteAvsAiException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public RenteAvsAiSearch search(RenteAvsAiSearch renteAvsAiSearch) throws RenteAvsAiException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            La renteAvsAi � mettre � jour
     * @return La renteAvsAi mis � jour
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAvsAi update(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException;
}
