package globaz.cygnus.helpers.process;

import globaz.cygnus.vb.process.RFImporterAvasadViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;

/**
 * 
 * Remplacé par l'écran des steps
 * 
 * @author jje
 */
@Deprecated
public class RFImporterAvasadHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RFImporterAvasadViewBean vb = (RFImporterAvasadViewBean) viewBean;

        Map<Enum<?>, String> properties = new HashMap<Enum<?>, String>();
        // properties.put(RFProcessImportationAvasadEnum.FILE_PATH_FOR_POPULATION, vb.getPathFile());
        // properties.put(RFProcessImportationAvasadEnum.EMAIL, vb.getEMailAddress());
        // properties.put(RFProcessImportationAvasadEnum.GESTIONNAIRE, vb.getUserProcess());

        RFImporterAvasadJob job = new RFImporterAvasadJob();

        job.setPathFile(vb.getPathFile());
        job.setSession(((BSession) session));
        // job.setKeyProcess(RFProcessImportationAvasadEnum.PROCESS_KEY_IMPORTATION_AVASAD.toLabel());
        job.setProperties(properties);

        try {

            JadeThreadActivator.startUsingJdbcContext(this, (initContext(job.getSession())));
            JadeThread.storeTemporaryObject("bsession", job.getSession());
            // JadeProcessServiceLocator.getPropertiesService().saveProperties(this.idExecutionProcess,
            // this.properties);
            JadeProcessServiceLocator.getJadeProcessCommonService().startJadeProcess(job, true);
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    private final JadeContext initContext(BSession session) throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return ctxtImpl;
    }

}
