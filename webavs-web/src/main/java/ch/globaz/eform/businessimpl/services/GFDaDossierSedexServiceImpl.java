package ch.globaz.eform.businessimpl.services;

import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import ch.globaz.eform.process.GFDaDossierSedexEnvoiDemandeProcess;
import ch.globaz.eform.process.GFDaDossierSedexEnvoiReponseProcess;
import globaz.globall.db.BSession;

import java.nio.file.Path;
import java.util.List;

public class GFDaDossierSedexServiceImpl implements GFDaDossierSedexService {
    @Override
    public void envoyerDemande(GFDaDossierModel model, BSession session) throws Exception {
        GFDaDossierSedexEnvoiDemandeProcess process = new GFDaDossierSedexEnvoiDemandeProcess();
        process.setModel(model);
        process.start();
    }

    @Override
    public void envoyerReponse(GFDaDossierModel model, GFDocumentTypeDossier documentType, List<String> attachments, BSession session) throws Exception {
        GFDaDossierSedexEnvoiReponseProcess process = new GFDaDossierSedexEnvoiReponseProcess();
        process.setModel(model);
        process.setDocumentType(documentType);
        process.setAttachments(attachments);
        process.start();
    }
}
