package globaz.tucana.helpers.administration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.process.administrateur.TUValidationBouclementProcess;
import globaz.tucana.vb.administration.TUValidationBouclementViewBean;

/**
 * Helper administration
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUValidationBouclementHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUValidationBouclementHelper() {
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

        /*
         * action lancer
         */
        if ("lancer".equals(action.getActionPart())) {
            try {
                // lancement du process de suppression de numéro de passage
                if ("validationBouclement".equals(action.getClassPart())) {
                    TUValidationBouclementViewBean eViewBean = (TUValidationBouclementViewBean) viewBean;
                    TUValidationBouclementProcess process = new TUValidationBouclementProcess();
                    process.setSession((BSession) session);
                    process.setEMailAddress(eViewBean.getEMail());
                    process.setIdBouclement(eViewBean.getIdBouclement());
                    process.setAnnee(eViewBean.getAnnee());
                    process.setMois(eViewBean.getMois());
                    BProcessLauncher.start(process);
                    if (process.isOnError()) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(process.getMessage());
                    }
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
