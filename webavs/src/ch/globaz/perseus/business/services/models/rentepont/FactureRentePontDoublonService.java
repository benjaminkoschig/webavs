package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;

public interface FactureRentePontDoublonService extends JadeApplicationService {

    boolean factureSimilaireExiste(String idFacture, String idDossier, String dateFacture, String montantFacture,
            String sousTypeQD) throws JadePersistenceException;

    String checkDemandeExiste(String dateReception, String dateFacture, String datePriseEnCharge, String idDossier,
            String anneeQD) throws RentePontException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError;
}
