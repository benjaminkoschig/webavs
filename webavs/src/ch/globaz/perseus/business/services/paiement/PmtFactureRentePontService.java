/**
 * 
 */
package ch.globaz.perseus.business.services.paiement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;

/**
 * @author DDE
 * 
 */
public interface PmtFactureRentePontService extends JadeApplicationService {

    public void comptabiliserLot(Lot lot, JadeBusinessLogSession logSession) throws PaiementException,
            JadePersistenceException;

    /**
     * Retourne une liste des factures par membre famille, dossier et par année présentes dans un lot (à utiliser pour
     * faire la décision décompte de facture) Retourne une map avec la clé "idDossier,idMembreFamille,anneeQd" et un
     * liste de facture.
     * 
     * @param lot
     * @return
     * @throws PaiementException
     * @throws JadePersistenceException
     */
    public Map<String, List<FactureRentePont>> groupListFactureByDossierRP(Lot lot) throws PaiementException,
            JadePersistenceException;

}
