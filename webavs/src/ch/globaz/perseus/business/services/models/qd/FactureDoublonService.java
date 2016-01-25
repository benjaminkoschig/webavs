package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface FactureDoublonService extends JadeApplicationService {

    boolean factureSimilaireExiste(String idFacture, String idQD, String dateFacture, String montantFacture)
            throws JadePersistenceException;

}
