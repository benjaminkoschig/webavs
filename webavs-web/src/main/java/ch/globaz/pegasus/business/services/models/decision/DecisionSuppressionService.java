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
     * Compte le nombre d'occurence de l'entit�
     * 
     * @param search
     * @return le nombre d'occurence de l'entit�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(DecisionSuppressionSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Cr�ation d'une entit� DecisionSuppression
     * 
     * @param decision
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationException
     */
    public DecisionSuppression create(DecisionSuppression decision) throws JadePersistenceException, DecisionException,
            JadeApplicationException, JadeCloneModelException;

    /**
     * Cr�ation d'une entit� DecisionSuppression
     * 
     * @param decision
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationException
     */
    public DecisionSuppression create(DecisionSuppression decision, boolean checkForCreate)
            throws JadePersistenceException, DecisionException, JadeApplicationException, JadeCloneModelException;

    /**
     * Suppression d'une entit�
     * 
     * @param decision
     * @return l'entit� supprim�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public DecisionSuppression delete(DecisionSuppression decision) throws DecisionException, JadePersistenceException;

    /**
     * Suppression de plusieurs entit�s
     * 
     * @param decisionSearch
     *            mod�le de recherche
     * @return nombre d'entit�s supprim�es
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int delete(DecisionSuppressionSearch decisionSearch) throws DecisionException, JadePersistenceException;

    /**
     * Chargement d'une entit�
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionSuppression read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche en fonction des param�tres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectu�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionSuppressionSearch search(DecisionSuppressionSearch decisionSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * Mise � jour d'une entit�
     * 
     * @param decision
     * @return l'entit� mise � jour
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeNoBusinessLogSessionError
     * @throws PmtMensuelException
     */
    public DecisionSuppression update(DecisionSuppression decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException, PmtMensuelException, JadeNoBusinessLogSessionError;

    /**
     * Mise � jour de l'entit� pour la pr�validation
     * 
     * @param decision
     * @return L'entit� mise � jour
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
     * Peemet la mise � jour d'une entit� pour la validation
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
