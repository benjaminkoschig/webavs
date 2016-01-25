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
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RenteAvsAiSearch search) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param renteAvsAi
     *            La renteAvsAi à créer
     * @return La renteAvsAi créé
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RenteAvsAi create(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            La renteAvsAi à supprimer
     * @return La renteAvsAi supprimé
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RenteAvsAi delete(RenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idrenteAvsAi
     *            L'identifiant de la renteAvsAie à charger en mémoire
     * @return La renteAvsAi chargé en mémoire
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param renteAvsAiSearch
     * @return La recherche effectué
     * @throws RenteAvsAiException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */
    public RenteAvsAiSearch search(RenteAvsAiSearch renteAvsAiSearch) throws RenteAvsAiException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            La renteAvsAi à mettre à jour
     * @return La renteAvsAi mis à jour
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RenteAvsAi update(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException;
}
