/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecisionSearch;

/**
 * @author SCE
 * 
 *         13 sept. 2010
 */
public interface SimpleValidationService extends JadeApplicationService {

    /**
     * Permet de compter le nombre de validation de décision
     * 
     * @param search
     * @return le nombre d'occurence de decisions
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(SimpleValidationDecisionSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Permet la création d'une validation
     * 
     * @param validationDecision
     * @return la validation crée
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleValidationDecision create(SimpleValidationDecision validationDecision)
            throws JadePersistenceException, DecisionException;

    /**
     * Attention aucun check n'est fait sur la liste idsDecisionHeader.
     * 
     * @param list
     *            des id de la simpleDesionHeade
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public void delete(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException;

    /**
     * Permet la suppression d'une validation
     * 
     * @param validationDecision
     * @return la validation supprimer
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleValidationDecision delete(SimpleValidationDecision validationDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * Suppression des validations via un modèle de recherche
     * 
     * @param validationDecisionSearch
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int delete(SimpleValidationDecisionSearch validationDecisionSearch) throws DecisionException,
            JadePersistenceException;

    /**
     * Permet le chargement d'une entité validation
     * 
     * @param idValidationDecision
     * @return l'entité chargée
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleValidationDecision read(String idValidationDecision) throws JadePersistenceException,
            DecisionException;

    /**
     * Permet la recherche de validation
     * 
     * @param validationDecisionSearch
     * @return la recherche retournée
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleValidationDecisionSearch search(SimpleValidationDecisionSearch validationDecisionSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * Permet la mise à jour d'une validation
     * 
     * @param decisionValidation
     * @return La validation mise à jour
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleValidationDecision update(SimpleValidationDecision decisionValidation)
            throws JadePersistenceException, DecisionException;
}
