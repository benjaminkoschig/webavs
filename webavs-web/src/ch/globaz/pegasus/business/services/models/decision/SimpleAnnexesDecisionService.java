/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecisionSearch;

/**
 * @author SCE
 * 
 *         26 ao�t 2010
 */
public interface SimpleAnnexesDecisionService extends JadeApplicationService {

    /**
     * Cretaion d'une entit� en base de donn�e
     * 
     * @param simpleAnnexesDecision
     * @return l'entit� cr�e
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleAnnexesDecision create(SimpleAnnexesDecision simpleAnnexesDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simpleAnnexesDecision
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleAnnexesDecision delete(SimpleAnnexesDecision simpleAnnexesDecision) throws JadePersistenceException,
            DecisionException;

    /**
     * Suppression des annexes par lots
     * 
     * @param lotAnnexes
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteByLots(JadeAbstractModel[] lotAnnexes) throws JadePersistenceException, DecisionException;

    /**
     * Attention aucun check n'est fait sur la liste idsDecisionHeader.
     * 
     * @param list
     *            des id de la simpleDesionHeader
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public void deleteForDecision(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException;

    /**
     * Suppression de toutes les annexes li�es � une decision
     * 
     * @param idDecisionHeader
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteForDecision(String idDecisionHeader) throws JadePersistenceException, DecisionException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idAnnexesDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleAnnexesDecision read(String idAnnexesDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param annexesDecisionSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleAnnexesDecisionSearch search(SimpleAnnexesDecisionSearch annexesDecisionSearch)
            throws JadePersistenceException, DecisionException;
}
