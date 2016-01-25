package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAi;

/**
 * Interface pour le service des simple Indemnites Journalieres AI 6.2010
 * 
 * @author SCE
 * 
 */
public interface SimpleIndemniteJournaliereAiService extends JadeApplicationService {

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
    public SimpleIndemniteJournaliereAi create(SimpleIndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

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
    public SimpleIndemniteJournaliereAi delete(SimpleIndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire un simpleIndemniteJournaliere
     * 
     * @param idIndemniteJournaliere
     *            L'identifiant du simpleIndemniteJournaliere � charger en m�moire
     * @return Le simpleIndemniteJournaliere charg� en m�moire
     * @throws IndemniteJournaliereAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleIndemniteJournaliereAi read(String idIndemniteJournaliere) throws IndemniteJournaliereAiException,
            JadePersistenceException;

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
    public SimpleIndemniteJournaliereAi update(SimpleIndemniteJournaliereAi indemniteJournaliere)
            throws IndemniteJournaliereAiException, JadePersistenceException;
}
