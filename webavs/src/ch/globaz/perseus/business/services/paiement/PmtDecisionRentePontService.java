/**
 * 
 */
package ch.globaz.perseus.business.services.paiement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;

/**
 * @author DDE
 * 
 */
public interface PmtDecisionRentePontService extends JadeApplicationService {

    public void comptabiliserLot(Lot lot, JadeBusinessLogSession logSession) throws PaiementException,
            JadePersistenceException;

}
