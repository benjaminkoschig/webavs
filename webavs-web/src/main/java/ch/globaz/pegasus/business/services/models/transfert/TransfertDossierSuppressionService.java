/**
 * 
 */
package ch.globaz.pegasus.business.services.models.transfert;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppressionSearch;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public interface TransfertDossierSuppressionService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence de l'entit�
     * 
     * @param search
     * @return le nombre d'occurence de l'entit�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(TransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException;

    /**
     * Cr�ation d'une entit� SimpleDecisionSuppression
     * 
     * @param decision
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression create(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Suppression d'une entit�
     * 
     * @param decision
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression delete(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Chargement d'une entit�
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression read(String idDecision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherche en fonction des param�tres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectu�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public TransfertDossierSuppressionSearch search(TransfertDossierSuppressionSearch decisionSearch)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Mise � jour d'une entit�
     * 
     * @param decision
     * @return l'entit� mise � jour
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression update(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;
}
