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
     * Compte le nombre d'occurence de l'entité
     * 
     * @param search
     * @return le nombre d'occurence de l'entité
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(TransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException;

    /**
     * Création d'une entité SimpleDecisionSuppression
     * 
     * @param decision
     * @return l'entité crée
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression create(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Suppression d'une entité
     * 
     * @param decision
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression delete(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Chargement d'une entité
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression read(String idDecision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectué
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public TransfertDossierSuppressionSearch search(TransfertDossierSuppressionSearch decisionSearch)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Mise à jour d'une entité
     * 
     * @param decision
     * @return l'entité mise à jour
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public TransfertDossierSuppression update(TransfertDossierSuppression decision) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException;
}
