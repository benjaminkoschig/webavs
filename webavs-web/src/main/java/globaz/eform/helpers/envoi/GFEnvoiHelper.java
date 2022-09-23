package globaz.eform.helpers.envoi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.businessimpl.services.sedex.envoi.EnvoiSedexService;
import ch.globaz.eform.constant.GFDocumentTypeDossier;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.servlet.GFEnvoiServletAction;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

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
        super.beforeExecute(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof GFEnvoiViewBean && GFEnvoiServletAction.ACTION_ENVOYER.equals(action.getActionPart())) {
            try {
                GFDaDossierSedexService sedexService = GFEFormServiceLocator.getGFDaDossierSedexService();
                sedexService.envoyerReponse(((GFEnvoiViewBean) viewBean).getDaDossier(),
                        GFDocumentTypeDossier.getDocumentTypeDossierByDocumentType(((GFEnvoiViewBean) viewBean).getTypeDeFichier()),
                        ((GFEnvoiViewBean) viewBean).getFileNameList().stream()
                                .map(fileName -> Paths.get(
                                        GFFileUtils.WORK_PATH +
                                                ((GFEnvoiViewBean) viewBean).getFolderUid() + "/" +
                                                FilenameUtils.removeExtension(((GFEnvoiViewBean) viewBean).getFileNamePersistance()) + "/" +
                                                fileName))
                                .collect(Collectors.toList()),
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
            GFDaDossierModel model = ((GFEnvoiViewBean) viewBean).getDaDossier();

            try {
                model.setType(GFTypeDADossier.SEND_TYPE.getCodeSystem());
                model.setStatus(GFStatusDADossier.SEND.getCodeSystem());
                if (StringUtils.isBlank(model.getId())) {
                    GFEFormServiceLocator.getGFDaDossierDBService().create(model);
                } else {
                    GFEFormServiceLocator.getGFDaDossierDBService().update(model);
                }
            } catch (JadeApplicationServiceNotAvailableException | JadePersistenceException e) {
                throw new RuntimeException(e);
            }
        }

        super.afterExecute(viewBean, action, session);
    }
}
