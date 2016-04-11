/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppressionSearch;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public interface SimpleDecisionSuppressionService extends JadeApplicationService {
    /**
     * Compte le nombre d'occurence de l'entité
     * 
     * @param search
     * @return le nombre d'occurence de l'entité
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleDecisionSuppressionSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Création d'une entité SinpleDecisionSuppression
     * 
     * @param decision
     * @return l'entité crée
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression create(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError;

    /**
     * Suppression d'une entité
     * 
     * @param decision
     * @return l'entité supprimé
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleDecisionSuppression delete(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException;

    /**
     * Chargement d'une entité
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * @param idDecisionHeader
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression readByIdDecisionHeader(String idDecisionHeader) throws JadePersistenceException,
            DecisionException;

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectué
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppressionSearch search(SimpleDecisionSuppressionSearch decisionSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * Mise à jour d'une entité
     * 
     * @param decision
     * @return l'entité mise à jour
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleDecisionSuppression update(SimpleDecisionSuppression decision) throws JadePersistenceException,
            DecisionException, PmtMensuelException, JadeApplicationServiceNotAvailableException,
            JadeNoBusinessLogSessionError;
}
