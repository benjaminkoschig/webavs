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
     * Compte le nombre d'occurence de l'entité
     * 
     * @param search
     * @return le nombre d'occurence de l'entité
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleTransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException;

    /**
     * Création d'une entité SinpleDecisionSuppression
     * 
     * @param decision
     * @return l'entité crée
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression create(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Suppression d'une entité
     * 
     * @param decision
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression delete(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Chargement d'une entité
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression read(String idDecision) throws JadePersistenceException,
            TransfertDossierException;

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param decisionSearch
     * @return la recherche effectué
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleTransfertDossierSuppressionSearch search(SimpleTransfertDossierSuppressionSearch decisionSearch)
            throws JadePersistenceException, TransfertDossierException;

    /**
     * Mise à jour d'une entité
     * 
     * @param decision
     * @return l'entité mise à jour
     * @throws JadePersistenceException
     * @throws TransfertDossierException
     */
    public SimpleTransfertDossierSuppression update(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException;
}
