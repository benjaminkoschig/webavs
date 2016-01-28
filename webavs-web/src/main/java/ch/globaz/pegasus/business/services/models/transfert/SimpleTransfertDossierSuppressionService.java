/**
 * 
 */
package ch.globaz.pegasus.business.services.models.transfert;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppressionSearch;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public interface SimpleTransfertDossierSuppressionService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence de l'entit�
     * 
     * @param search
     * @return le nombre d'occurence de l'entit�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleTransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException;

    /**
     * Cr�ation d'une entit� SinpleDecisionSuppression
     * 
     * @param decision
     * @return l'entit� cr�e
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression create(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Suppression d'une entit�
     * 
     * @param decision
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression delete(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Chargement d'une entit�
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression read(String idDecision) throws JadePersistenceException,
            TransfertDossierException;

    /**
     * Recherche en fonction des param�tres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectu�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleTransfertDossierSuppressionSearch search(SimpleTransfertDossierSuppressionSearch decisionSearch)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Mise � jour d'une entit�
     * 
     * @param decision
     * @return l'entit� mise � jour
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression update(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;
}
