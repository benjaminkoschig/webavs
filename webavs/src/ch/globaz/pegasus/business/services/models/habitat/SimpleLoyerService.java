package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyer;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyerSearch;

public interface SimpleLoyerService extends JadeApplicationService {

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
    public SimpleLoyer create(SimpleLoyer simpleLoyer) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException;

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
     */
    public SimpleLoyer delete(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleLoyer en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleLoyer
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

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
    public SimpleLoyer read(String idSimpleLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param renteAvsAiSearch
     * @return La recherche effectué
     * @throws LoyerException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public SimpleLoyerSearch search(SimpleLoyerSearch loyerSearch) throws LoyerException, DonneeFinanciereException,
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
    public SimpleLoyer update(SimpleLoyer simpleLoyer) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException;
}
