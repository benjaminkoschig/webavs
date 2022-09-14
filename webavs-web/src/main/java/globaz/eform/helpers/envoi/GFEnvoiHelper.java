package globaz.eform.helpers.envoi;

import ch.globaz.eform.businessimpl.services.sedex.envoi.EnvoiSedexService;
import ch.globaz.eform.web.servlet.GFEnvoiServletAction;
import globaz.eform.itext.GFDocumentPojo;
import globaz.eform.itext.GFEnvoiDossier;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.publish.client.JadePublishDocument;

public class GFEnvoiHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        String actionPart = action.getActionPart();
        if (viewBean instanceof GFEnvoiViewBean && GFEnvoiServletAction.ACTION_ENVOYER.equals(actionPart)) {
            // Génération du document LEAD
            GFEnvoiDossier documentEnvoie = new GFEnvoiDossier();
            documentEnvoie.setSession((BSession) session);
            documentEnvoie.setDocumentPojo(GFDocumentPojo.buildFromGFEnvoiViewBean((GFEnvoiViewBean) viewBean));
            try {
                documentEnvoie.executeProcess();

                JadePublishDocument getDocument = (JadePublishDocument)documentEnvoie.getAttachedDocuments().get(0);

                // Création de l'envoie SEDEX
                EnvoiSedexService envoiSedexService = new EnvoiSedexService((GFEnvoiViewBean) viewBean);
                envoiSedexService.setDocumentLead(getDocument.getDocumentLocation());
                envoiSedexService.createSedexMessage();
                envoiSedexService.sendMessage();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }
}
