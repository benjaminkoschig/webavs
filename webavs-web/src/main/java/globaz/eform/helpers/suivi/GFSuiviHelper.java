package globaz.eform.helpers.suivi;

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
import globaz.eform.vb.demande.GFDemandeViewBean;
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
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setWhereKey("tracking");
        search.setByType(GFTypeDADossier.RECEPTION.getCodeSystem());
        search.setByStatus(GFStatusDADossier.TREAT.getCodeSystem());

        GFEFormServiceLocator.getGFDaDossierDBService().search(search);
        EFormFileService fileService = new EFormFileService(GFApplication.DA_DOSSIER_HOST_FILE_SERVER);

        Arrays.stream(search.getSearchResults()).map(o -> (GFDaDossierModel) o)
                .forEach(daDossierModel -> {
                    String sftpPath = GFFileUtils.generateDaDossierFilePath(daDossierModel);

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
