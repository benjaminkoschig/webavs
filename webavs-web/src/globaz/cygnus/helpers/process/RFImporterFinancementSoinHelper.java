/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.vb.process.RFImporterFinancementSoinViewBean;
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
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;

/**
 * <H1>Description</H1>
 * 
 * @author LFO
 */
public class RFImporterFinancementSoinHelper extends FWHelper {

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        /*
         * RFPreparerDecisionsViewBean vb = (RFPreparerDecisionsViewBean) viewBean;
         * 
         * RFPreparerDecisionsProcess process = new RFPreparerDecisionsProcess(); process.setSession((BSession)
         * session);
         * 
         * process.setEMailAddress(vb.getEMailAddress()); process.setDateSurDocument(vb.getDateSurDocument());
         * process.setIdGestionnaire(vb.getIdGestionnaire());
         * 
         * process.start();
         */

        RFImporterFinancementSoinViewBean vb = (RFImporterFinancementSoinViewBean) viewBean;

        Map<Enum<?>, String> properties = new HashMap<Enum<?>, String>();
        properties.put(RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION, vb.getPathFile());
        properties.put(RFProcessImportFinancementSoinEnum.EMAIL, vb.getEMailAddress());
        properties.put(RFProcessImportFinancementSoinEnum.GESTIONNAIRE, vb.getUserProcess());

        RFimportFinancementSoinJob job = new RFimportFinancementSoinJob();

        job.setPathFile(vb.getPathFile());
        job.setSession(((BSession) session));
        job.setKeyProcess("Cygnus.import.financement.soin");
        job.setProperties(properties);

        try {

            JadeThreadActivator.startUsingJdbcContext(this, (initContext(job.getSession())));
            JadeThread.storeTemporaryObject("bsession", job.getSession());
            JadeProcessServiceLocator.getJadeProcessCommonService().startJadeProcess(job, true);

        } catch (JadeApplicationServiceNotAvailableException e) {

            e.printStackTrace();
        } catch (Exception e) {
            // En cas d'importation a double, envoi d'un mail d'information.
            try {
                BSession bSession = (BSession) session;
                JadeSmtpClient.getInstance().sendMail(vb.getEMailAddress(),
                        bSession.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_TITRE_ERREUR_IMPORTATION_A_DOUBLE"),
                        loadMessage(bSession, vb.getFileName(), e).toString(), null);
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
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

    private StringBuffer loadMessage(BSession session, String fileName, Exception e) {
        StringBuffer message = new StringBuffer();

        // Tronk le nom de fichier pour ne pas afficher le répertoire dans lequel il a été récupéré
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

        message.append(PRStringUtils.replaceString(
                session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_ERREUR_IMPORTATION_A_DOUBLE"), "fileName", fileName));

        return message;

    }
}
