package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface LoyerService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AbstractDonneeFinanciereSearchModel search) throws LoyerException, JadePersistenceException;

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param renteAvsAi
     *            La renteAvsAi à créer
     * @return le loyer créé
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Loyer create(Loyer loyer) throws LoyerException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            le loyer à supprimer
     * @return le loyer supprimé
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public Loyer delete(Loyer loyer) throws LoyerException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idrenteAvsAi
     *            L'identifiant de le loyere à charger en mémoire
     * @return le loyer chargé en mémoire
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Loyer read(String idLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Chargement d'un Loyer via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    public Loyer readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws LoyerException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            le loyer à mettre à jour
     * @return le loyer mis à jour
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Loyer update(Loyer loyer) throws LoyerException, DonneeFinanciereException, JadePersistenceException;

}
