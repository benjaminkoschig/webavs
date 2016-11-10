package globaz.corvus.helpers.process;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REDiminutionRenteAccordeeProcess;
import globaz.corvus.vb.process.REDiminutionRenteAccordeeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.helpers.PRAbstractHelper;

public class REDiminutionRenteAccordeeHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession iSession) {

        REDiminutionRenteAccordeeViewBean vb = (REDiminutionRenteAccordeeViewBean) viewBean;
        REDiminutionRenteAccordeeProcess process = null;
        BSession session = (BSession) iSession;

        // Check des erreurs contenues dans le viewBean
        if (!JadeStringUtil.isEmpty(vb.getErrorMsg())) {
            String message = session.getLabel("ERROR_IMPOSSIBLE_EXECUTER_PROCESS_A_CAUSE_DES_ERREURS_SUIVANTES");
            message += " : " + vb.getErrorMsg();
            throw new RETechnicalException(message);
        }

        // Check des warnings contenues dans le viewBean
        if (!JadeStringUtil.isEmpty(vb.getWarningMsg())) {
            String message = session.getLabel("ERROR_IMPOSSIBLE_EXECUTER_PROCESS_A_CAUSE_DES_ERREURS_SUIVANTES");
            message += " : " + vb.getWarningMsg();
            throw new RETechnicalException(message);
        }

        try {
            process = new REDiminutionRenteAccordeeProcess();
            process.setSession(session);
            process.setIdRenteAccordee(vb.getIdRenteAccordee());
            process.setCsCodeMutation(vb.getCsCodeMutation());
            process.setCsCodeTraitement(vb.getCsCodeTraitement());
            process.setDateFinDroit(vb.getDateFinDroit());
            process.setEMailAddress(session.getUserEMail());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(e.toString());
        } finally {
            // BZ 4232 : Comme le process n'est pas envoyé au serveur de job, envoyer le mail manuellement.
            try {

                if (process != null) {
                    JadeSmtpClient.getInstance().sendMail(process.getEMailAddress(), process.getEMailObject(),
                            process.getMemoryLog().getMessagesInString(), null);
                }
            } catch (Exception e) {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(e.toString());
            }
        }
    }
}
