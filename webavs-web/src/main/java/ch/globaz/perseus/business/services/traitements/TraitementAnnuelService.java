/**
 * 
 */
package ch.globaz.perseus.business.services.traitements;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.models.decision.Decision;

/**
 * @author dde
 * 
 */
public interface TraitementAnnuelService extends JadeApplicationService {

    /**
     * Exéctue le traintement annuel sur les demandes en cours.
     * 
     * @param session
     * @param logSession
     * @return liste des décisions qui ont été validées qu'il faudra imprimer
     * @throws PerseusException
     * @throws JadePersistenceException
     */
    public List<Decision> executerTraitementsAnnuels(BSession session, JadeBusinessLogSession logSession,
            String texteDecision) throws Exception;

    public List<Decision> executerTraitementsAnnuelsAvecAF(BSession session, JadeBusinessLogSession logSession,
            String texteDecision) throws Exception;

}
