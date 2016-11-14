package globaz.cygnus.helpers.process;

import globaz.cygnus.vb.process.RFImportDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.tools.PRStringUtils;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;

public abstract class RFImporterDemandeHelper extends FWHelper {

    public abstract String getProcessKey();

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RFImportDemandeViewBean vb = (RFImportDemandeViewBean) viewBean;

        Map<Enum<?>, String> properties = new HashMap<Enum<?>, String>();
        properties.put(RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION, vb.getPathFile());
        properties.put(RFProcessImportFinancementSoinEnum.EMAIL, vb.getEMailAddress());
        properties.put(RFProcessImportFinancementSoinEnum.GESTIONNAIRE, vb.getUserProcess());

        RFimportDemandeJob job = new RFimportDemandeJob();

        job.setSession((BSession) session);
        job.setKeyProcess(getProcessKey());
        job.setProperties(properties);

        try {
            BSessionUtil.initContext((BSession) session, this);
            JadeThread.storeTemporaryObject("bsession", job.getSession());
            JadeProcessServiceLocator.getJadeProcessCommonService().startJadeProcess(job, true);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new java.lang.RuntimeException(e);
        } catch (Exception e) {
            // En cas d'importation a double, envoi d'un mail d'information.
            try {
                BSession bSession = (BSession) session;
                JadeSmtpClient.getInstance().sendMail(vb.getEMailAddress(),
                        bSession.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_TITRE_ERREUR_IMPORTATION_A_DOUBLE"),
                        loadMessage(bSession, vb.getFileName(), e).toString(), null);
                JadeLogger.error("Error in process a mail is sended", e);
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        } finally {
            BSessionUtil.stopUsingContext(this);
        }
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
