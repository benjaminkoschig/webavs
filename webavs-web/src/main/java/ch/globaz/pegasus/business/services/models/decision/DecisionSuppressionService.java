/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public interface DecisionSuppressionService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence de l'entité
     * 
     * @param search
     * @return le nombre d'occurence de l'entité
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(DecisionSuppressionSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Création d'une entité DecisionSuppression
     * 
     * @param decision
     * @return l'entité crée
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationException
     */
    public DecisionSuppression create(DecisionSuppression decision) throws JadePersistenceException, DecisionException,
            JadeApplicationException, JadeCloneModelException;

    /**
     * Création d'une entité DecisionSuppression
     * 
     * @param decision
     * @return l'entité crée
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationException
     */
    public DecisionSuppression create(DecisionSuppression decision, boolean checkForCreate)
            throws JadePersistenceException, DecisionException, JadeApplicationException, JadeCloneModelException;

    /**
     * Suppression d'une entité
     * 
     * @param decision
     * @return l'entité supprimé
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public DecisionSuppression delete(DecisionSuppression decision) throws DecisionException, JadePersistenceException;

    /**
     * Suppression de plusieurs entités
     * 
     * @param decisionSearch
     *            modèle de recherche
     * @return nombre d'entités supprimées
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int delete(DecisionSuppressionSearch decisionSearch) throws DecisionException, JadePersistenceException;

    /**
     * Chargement d'une entité
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectué
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionSuppressionSearch search(DecisionSuppressionSearch decisionSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * Mise à jour d'une entité
     * 
     * @param decision
     * @return l'entité mise à jour
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeNoBusinessLogSessionError
     * @throws PmtMensuelException
     */
    public DecisionSuppression update(DecisionSuppression decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException, PmtMensuelException, JadeNoBusinessLogSessionError;

    /**
     * Mise à jour de l'entité pour la prévalidation
     * 
     * @param decision
     * @return L'entité mise à jour
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     * @throws PmtMensuelException
     */
    // public DecisionSuppression updateForPrevalidation(DecisionSuppression decision) throws DecisionException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException,
    // JadeNoBusinessLogSessionError;

    /**
     * Peemet la mise à jour d'une entité pour la validation
     * 
     * @param decision
     * @return
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    // public DecisionSuppression updateForValidation(DecisionSuppression decision) throws DecisionException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException, DossierException,
    // PCAccordeeException, JadePersistenceException, JadeCloneModelException, JadeApplicationException;
}
