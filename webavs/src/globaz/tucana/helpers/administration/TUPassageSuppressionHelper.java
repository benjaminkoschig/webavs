package globaz.tucana.helpers.administration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.process.administrateur.TUPassageSuppressionProcess;
import globaz.tucana.vb.administration.TUPassageSuppressionViewBean;

/**
 * Helper administration
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUPassageSuppressionHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUPassageSuppressionHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // -------------------------------------------------------------------------------------------------------------------------------------------------
        // ACTION LANCER
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        if ("lancer".equals(action.getActionPart())) {
            try {
                // lancement du process de suppression de numéro de passage
                if ("passageSuppression".equals(action.getClassPart())) {
                    TUPassageSuppressionViewBean eViewBean = (TUPassageSuppressionViewBean) viewBean;
                    TUPassageSuppressionProcess process = new TUPassageSuppressionProcess();
                    process.setCsApplication(eViewBean.getCsApplication());
                    process.setNoPassage(eViewBean.getNoPassage());
                    process.setCsSuppressionReferenceAF(eViewBean.getCsSuppressionReferenceAF());
                    process.setSession((BSession) session);
                    process.setEMailAddress(eViewBean.getEMail());
                    BProcessLauncher.start(process);
                    if (process.isOnError()) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(process.getMessage());
                    }
                    // lancmenet du process d'importation d'un bouclement
                } else {
                    return super.execute(viewBean, action, session);
                }

            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

}
