package globaz.eform.helpers.envoi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.eform.web.servlet.GFEnvoiServletAction;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeUUIDGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class GFEnvoiHelper extends FWHelper {
    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        GFEnvoiViewBean envoiViewBean = (GFEnvoiViewBean) viewBean;

        if (!StringUtils.isBlank(envoiViewBean.getId())) {
            GFDaDossierSearch search = new GFDaDossierSearch();
            search.setWhereKey("default");
            search.setById(envoiViewBean.getId());

            GFEFormServiceLocator.getGFDaDossierDBService().search(search);
            if (search.getSize() == 1) {
                envoiViewBean.setDaDossier((GFDaDossierModel) search.getSearchResults()[0]);
            }
        } else {
            envoiViewBean.getDaDossier().setMessageId(UUID.randomUUID().toString());
            envoiViewBean.getDaDossier().setOurBusinessRefId(JadeUUIDGenerator.createLongUID().toString());
        }


        if (GFEnvoiServletAction.ACTION_ENVOYER.equals(action.getActionPart())) {

            //Mise en dossier de partage des fichiers à joindre à la réponse de la demande de transfère
            if (!envoiViewBean.getFileNameList().isEmpty()) {
                EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_PARTAGE_FILE);

                if (!fileService.exist(envoiViewBean.getDaDossier().getMessageId())) {
                    fileService.createFolder(envoiViewBean.getDaDossier().getMessageId());
                }

                envoiViewBean.getFileNameList().stream()
                        .map(path -> Paths.get(GFFileUtils.WORK_PATH +
                                ((GFEnvoiViewBean) viewBean).getFolderUid() + File.separator +
                                path))
                        .forEach(path -> fileService
                                .send(path, envoiViewBean.getDaDossier().getMessageId() + File.separator));
            }
        }

        super.beforeExecute(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof GFEnvoiViewBean && GFEnvoiServletAction.ACTION_ENVOYER.equals(action.getActionPart())) {
            try {
                GFDaDossierSedexService sedexService = GFEFormServiceLocator.getGFDaDossierSedexService();
                sedexService.envoyerReponse(((GFEnvoiViewBean) viewBean).getDaDossier(),
                        GFDocumentTypeDossier.getDocumentTypeDossierByDocumentType(((GFEnvoiViewBean) viewBean).getTypeDeFichier()),
                        ((GFEnvoiViewBean) viewBean).getFileNameList(),
                        ((GFEnvoiViewBean) viewBean).getSession());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    @Override
    public void afterExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof GFEnvoiViewBean && GFEnvoiServletAction.ACTION_ENVOYER.equals(action.getActionPart())) {
            Path workDir = Paths.get(GFFileUtils.WORK_PATH + ((GFEnvoiViewBean) viewBean).getFolderUid() + File.separator);

            if (Files.exists(workDir)) {
                Files.delete(workDir);
            }
        }

        super.afterExecute(viewBean, action, session);
    }
}
