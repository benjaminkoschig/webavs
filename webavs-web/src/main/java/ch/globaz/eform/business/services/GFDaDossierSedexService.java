package ch.globaz.eform.business.services;

import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.nio.file.Path;
import java.util.List;

public interface GFDaDossierSedexService extends JadeApplicationService {
    void envoyerDemande(GFDaDossierModel model, BSession session) throws Exception;
    void envoyerReponse(GFDaDossierModel model, GFDocumentTypeDossier documentType, List<String> attachments, BSession session) throws Exception;
}
