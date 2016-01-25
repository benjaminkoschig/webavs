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
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere à créer
     * @return Le simpleIndemniteJournaliere créé
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleIndemniteJournaliereAi create(SimpleIndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere à supprimer
     * @return Le simpleIndemniteJournaliere supprimé
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleIndemniteJournaliereAi delete(SimpleIndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet la suppression réele de la donnée financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un simpleIndemniteJournaliere
     * 
     * @param idIndemniteJournaliere
     *            L'identifiant du simpleIndemniteJournaliere à charger en mémoire
     * @return Le simpleIndemniteJournaliere chargé en mémoire
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleIndemniteJournaliereAi read(String idIndemniteJournaliere) throws IndemniteJournaliereAiException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleIndemniteJournaliere
     *            Le simpleIndemniteJournaliere à mettre à jour
     * @return Le simpleIndemniteJournaliere mis à jour
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleIndemniteJournaliereAi update(SimpleIndemniteJournaliereAi indemniteJournaliere)
            throws IndemniteJournaliereAiException, JadePersistenceException;
}
