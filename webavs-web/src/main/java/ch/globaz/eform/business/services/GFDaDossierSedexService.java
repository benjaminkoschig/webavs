package ch.globaz.eform.business.services;

import ch.globaz.eform.business.models.GFDaDossierModel;
import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface GFDaDossierSedexService extends JadeApplicationService {
    void envoyerDemande(GFDaDossierModel model, BSession session) throws Exception;
}
