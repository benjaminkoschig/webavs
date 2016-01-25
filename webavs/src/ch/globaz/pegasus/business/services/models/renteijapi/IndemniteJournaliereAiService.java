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
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(IndemniteJournaliereAiSearch search) throws IndemniteJournaliereAiException,
            JadePersistenceException;

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
    public IndemniteJournaliereAi create(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

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
    public IndemniteJournaliereAi delete(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idIndemniteJournaliere
     *            L'identifiant du simpleIndemniteJournaliere à charger en mémoire
     * @return Le simpleIndemniteJournaliere chargé en mémoire
     * @throws IndemniteJournaliereAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param indemniteJournaliereAiSearch
     * @return La recherche effectué
     * @throws IndemniteJournaliereAiException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public IndemniteJournaliereAiSearch search(IndemniteJournaliereAiSearch indemniteJournaliereAiSearch)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

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
    public IndemniteJournaliereAi update(IndemniteJournaliereAi indemniteJournaliere)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException;

}
