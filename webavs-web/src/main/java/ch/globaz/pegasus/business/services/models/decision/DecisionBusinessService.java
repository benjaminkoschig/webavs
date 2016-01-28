/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.ListDecisions;

/**
 * @author SCE
 * 
 *         24 sept. 2010
 * 
 *         Service de base pour les d�cisions. Utilis� pour le rcListe
 */
public interface DecisionBusinessService extends JadeApplicationService {

    public String findDateDecision(String idVersionDroit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Charge l'entit� en base de donn�es
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public ListDecisions read(String idDecision) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

}
