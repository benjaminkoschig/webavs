package globaz.eform.helpers.suivi;

import ch.globaz.common.util.Dates;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.hosting.EFormFileService;
import ch.globaz.eform.utils.GFFileUtils;
import ch.globaz.eform.web.application.GFApplication;
import ch.globaz.eform.web.servlet.GFDemandeServletAction;
import ch.globaz.eform.web.servlet.GFFormulaireServletAction;
import ch.globaz.eform.web.servlet.GFSuiviServletAction;
import globaz.eform.vb.demande.GFDemandeViewBean;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.eform.vb.suivi.GFSuiviViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.nio.file.Paths;
import java.util.Arrays;

public class GFSuiviHelper extends FWHelper {
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if(GFSuiviServletAction.ACTION_STATUT.equals(action.getActionPart()) && (viewBean instanceof GFSuiviViewBean)) {
            GFSuiviViewBean bean = (GFSuiviViewBean) viewBean;
            try {
                GFDaDossierSearch search = new GFDaDossierSearch();
                search.setById(bean.getDaDossier().getId());

                GFDaDossierModel model = (GFDaDossierModel) GFEFormServiceLocator.getGFDaDossierDBService().search(search).getSearchResults()[0];

                model.setStatus(bean.getDaDossier().getStatus());
                GFEFormServiceLocator.getGFDaDossierDBService().update(model);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setWhereKey("tracking");
        search.setByStatus(GFStatusDADossier.TREAT.getCodeSystem());

        GFEFormServiceLocator.getGFDaDossierDBService().search(search);
        EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_HOST_FILE_SERVER);

        Arrays.stream(search.getSearchResults()).map(o -> (GFDaDossierModel) o)
                .forEach(daDossierModel -> {
                    String sftpPath = GFFileUtils.generateDaDossierFilePath(Dates.extractSpyDate(daDossierModel.getSpy()),
                            daDossierModel.getNssAffilier());

                    if (!fileService.exist(sftpPath)) {
                        daDossierModel.setStatus(GFStatusDADossier.ENDED.getCodeSystem());
                        try {
                            GFEFormServiceLocator.getGFDaDossierDBService().update(daDossierModel);
                        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        super.beforeExecute(viewBean, action, session);
    }
}
