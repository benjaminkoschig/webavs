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

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface DecisionHeaderService extends JadeApplicationService {

    /**
     * Compte le nombre d'occurence en base de donn�es
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
     * Charge l'entit� en base de donn�es
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionHeader read(String idDecision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    DecisionHeader readAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Recherche de l'entit� en base de donn�es
     * 
     * @param decisionSearch
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionHeaderSearch search(DecisionHeaderSearch decisionSearch) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public DecisionHeader update(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Met � jour les annexes et copies du decisionHeader. Note que toutes les anciennes donn�es d'annexes et copies
     * li�s � la d�cision sont �cras�es.
     * 
     * @param decision
     *            La d�cision � mettre � jour
     * @return la d�cision mise � jour
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    DecisionHeader updateAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;;

}
