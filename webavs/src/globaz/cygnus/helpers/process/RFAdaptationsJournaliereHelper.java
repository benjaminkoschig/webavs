/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFAdaptationsJournalieresProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFAdaptationsJournaliereViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFAdaptationsJournaliereHelper extends PRAbstractHelper {

    boolean validation = true;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RFAdaptationsJournaliereViewBean vb = (RFAdaptationsJournaliereViewBean) viewBean;
        try {

            if (validate(vb)) {

                RFAdaptationsJournalieresProcess adaptationProcess = new RFAdaptationsJournalieresProcess();
                adaptationProcess.setSession((BSession) session);
                // adaptationProcess.setTransaction(transactionAdaptation);
                adaptationProcess.setEMailAddress(vb.getEMailAddress());
                adaptationProcess.setIdGestionnaire(vb.getIdGestionnaire());
                adaptationProcess.setSendCompletionMail(true);
                adaptationProcess.setSendMailOnError(true);

                BProcessLauncher.start(adaptationProcess, false);

                // adaptationProcess.start();
            }

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Methode qui permet de controler que les champs obligatoires soient renseignés
     * 
     * @param viewBean
     * @return boolean
     * @throws Exception
     */
    private boolean validate(FWViewBeanInterface viewBean) throws Exception {

        // Adresse e-mail obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFAdaptationsJournaliereViewBean) viewBean).getEMailAddress())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_ADRESSE_MAIL_OBLIGATOIRE");
            validation = false;
        }

        return validation;
    }

}
