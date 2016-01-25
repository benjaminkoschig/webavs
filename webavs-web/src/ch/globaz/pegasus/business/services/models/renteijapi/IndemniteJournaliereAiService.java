package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * Interface pour le service des Indemnites Journalieres AI
 * 
 * @date 6.2010
 * 
 * @author SCE
 * 
 */
public interface IndemniteJournaliereAiService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(IndemniteJournaliereAiSearch search) throws IndemniteJournaliereAiException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere � cr�er
     * @return Le simpleIndemniteJournaliere cr��
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public IndemniteJournaliereAi create(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere � supprimer
     * @return Le simpleIndemniteJournaliere supprim�
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public IndemniteJournaliereAi delete(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idIndemniteJournaliere
     *            L'identifiant du simpleIndemniteJournaliere � charger en m�moire
     * @return Le simpleIndemniteJournaliere charg� en m�moire
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public IndemniteJournaliereAi read(String idIndemniteJournaliere) throws IndemniteJournaliereAiException,
            JadePersistenceException;

    /**
     * Chargement d'une ijai via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     */
    public IndemniteJournaliereAi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param indemniteJournaliereAiSearch
     * @return La recherche effectu�
     * @throws IndemniteJournaliereAiException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public IndemniteJournaliereAiSearch search(IndemniteJournaliereAiSearch indemniteJournaliereAiSearch)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere � mettre � jour
     * @return Le simpleIndemniteJournaliere mis � jour
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public IndemniteJournaliereAi update(IndemniteJournaliereAi indemniteJournaliere)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

}
