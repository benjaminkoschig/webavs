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
 *         26 août 2010
 */
public interface SimpleAnnexesDecisionService extends JadeApplicationService {

    /**
     * Cretaion d'une entité en base de donnée
     * 
     * @param simpleAnnexesDecision
     * @return l'entité crée
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public SimpleAnnexesDecision create(SimpleAnnexesDecision simpleAnnexesDecision) throws DecisionException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param simpleAnnexesDecision
     * @return l'entité supprimé
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
     * Suppression de toutes les annexes liées à une decision
     * 
     * @param idDecisionHeader
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void deleteForDecision(String idDecisionHeader) throws JadePersistenceException, DecisionException;

    /**
     * Permet le chargement d'une entité
     * 
     * @param idAnnexesDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleAnnexesDecision read(String idAnnexesDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la recherche d'entités
     * 
     * @param annexesDecisionSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public SimpleAnnexesDecisionSearch search(SimpleAnnexesDecisionSearch annexesDecisionSearch)
            throws JadePersistenceException, DecisionException;
}
