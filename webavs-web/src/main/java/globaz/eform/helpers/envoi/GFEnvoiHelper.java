package globaz.eform.helpers.envoi;

import ch.globaz.common.file.JadeFsServer;
import ch.globaz.eform.businessimpl.services.sedex.envoi.EnvoiSedexService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.servlet.GFEnvoiServletAction;
import globaz.eform.itext.GFDocumentPojo;
import globaz.eform.itext.GFEnvoiDossier;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.publish.client.JadePublishDocument;

import java.io.File;

public class GFEnvoiHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        String actionPart = action.getActionPart();
        if (viewBean instanceof GFEnvoiViewBean && GFEnvoiServletAction.ACTION_ENVOYER.equals(actionPart)) {
            GFEnvoiViewBean gFEnvoiViewBean = (GFEnvoiViewBean) viewBean;
            try {
                // Génération du document LEAD
                GFEnvoiDossier documentEnvoie = new GFEnvoiDossier();
                documentEnvoie.setSession((BSession) session);
                documentEnvoie.setDocumentPojo(GFDocumentPojo.buildFromGFEnvoiViewBean(gFEnvoiViewBean));
                documentEnvoie.executeProcess();
                JadePublishDocument getDocument = (JadePublishDocument)documentEnvoie.getAttachedDocuments().get(0);
                String destFile = GFFileUtils.WORK_PATH + gFEnvoiViewBean.getFolderUid()+ "/" + GFEnvoiDossier.FILENAME;
                JadeFsFacade.copyFile(getDocument.getDocumentLocation(), destFile);
                JadeFsFacade.delete(getDocument.getDocumentLocation());

                // Création de l'envoie SEDEX
                EnvoiSedexService envoiSedexService = new EnvoiSedexService(gFEnvoiViewBean);
                envoiSedexService.setDocumentLead(destFile);
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
