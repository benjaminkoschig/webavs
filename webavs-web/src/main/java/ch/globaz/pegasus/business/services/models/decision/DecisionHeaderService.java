/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionHeader;
import ch.globaz.pegasus.business.models.decision.DecisionHeaderSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface DecisionHeaderService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence en base de données
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(DecisionHeaderSearch search) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public DecisionHeader create(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public DecisionHeader delete(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;;

    /**
     * Charge l'entité en base de données
     * 
     * @param idDecision
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionHeader read(String idDecision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    DecisionHeader readAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Recherche de l'entité en base de données
     * 
     * @param decisionSearch
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionHeaderSearch search(DecisionHeaderSearch decisionSearch) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Met à jour les annexes et copies du decisionHeader. Note que toutes les anciennes données d'annexes et copies
     * liés à la décision sont écrasées.
     * 
     * @param decision
     *            La décision à mettre à jour
     * @return la décision mise à jour
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    DecisionHeader updateAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de chercher une version de droit depuis une décision header.
     * Si aucune version de droit n'est trouvé on retourne NULL.
     * Si trop de decision sont retrouvé pour trouver le droit une exception sera lancé.
     * 
     * @param decision
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    SimpleVersionDroit loadSimpleVersionDroit(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException;

}
