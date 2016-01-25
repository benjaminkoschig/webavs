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
public interface TraitementPonctuelService extends JadeApplicationService {

    /**
     * Ex�ctue le traintement annuel sur les demandes en cours.
     * 
     * @param session
     * @param logSession
     * @param mois
     *            mois courant
     * @param texteDecision
     *            texte de la d�cision
     * @return liste des d�cisions qui ont �t� valid�es qu'il faudra imprimer
     * @throws PerseusException
     * @throws JadePersistenceException
     */
    public List<Decision> executerTraitements(BSession session, JadeBusinessLogSession logSession, String mois,
            String texteDecision) throws PerseusException, JadePersistenceException;

}
