/**
 * 
 */
package ch.globaz.perseus.business.services.paiement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.LinkedHashMap;
import java.util.List;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.services.models.lot.PrestationService;

/**
 * @author DDE
 * 
 */
public interface PmtFactureService extends JadeApplicationService {

    public void comptabiliserLot(Lot lot, JadeBusinessLogSession logSession) throws PaiementException,
            JadePersistenceException;

    /**
     * Retourne une liste des factures par membre famille, dossier et par année présentes dans un lot (à utiliser pour
     * faire la décision décompte de facture) Retourne une map avec la clé "idDossier,idMembreFamille,anneeQd" et un
     * liste de facture.
     * 
     * @param lot
     * @param idUserAgence
     * @return
     * @throws PaiementException
     * @throws JadePersistenceException
     */
    public LinkedHashMap<String, List<Facture>> groupListFactureByMembreFamille(PrestationService service, Lot lot,
            List<String> idUserAgence, boolean isAgence) throws PaiementException, JadePersistenceException;

}
