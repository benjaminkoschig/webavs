/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.decision.DecisionHeader;
import ch.globaz.pegasus.business.models.decision.DecisionHeaderSearch;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public interface SimpleDecisionService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DecisionHeaderSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Permet la création d'une entité demande
     * 
     * @param demande
     *            La décsions à créer
     * @return La décision créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DecisionHeader create(DecisionHeader decision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la suppression d'une entité decision
     * 
     * @param decision
     *            a supprimer
     * @return La decision supprimé
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DecisionHeader delete(DecisionHeader decision) throws DecisionException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une decision
     * 
     * @param idDecision
     *            L'identifiant de la decision à charger en mémoire
     * @return La decision chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DecisionHeader read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet de chercher des demandes selon un modèle de critères.
     * 
     * @param decisionSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DecisionHeaderSearch search(DecisionHeaderSearch decisionSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * 
     * Permet la mise à jour d'une entité decision
     * 
     * @param decision
     *            La decision à mettre à jour
     * @return La deecision mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public DecisionHeader update(DecisionHeader decision) throws JadePersistenceException, DecisionException;

}