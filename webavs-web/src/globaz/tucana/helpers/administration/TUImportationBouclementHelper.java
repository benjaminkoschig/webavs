package globaz.tucana.helpers.administration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.application.TUApplication;
import globaz.tucana.process.administrateur.TUImportationBouclementProcess;
import globaz.tucana.vb.administration.TUImportationBouclementViewBean;

/**
 * Helper administration
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUImportationBouclementHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUImportationBouclementHelper() {
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
                if ("importationBouclement".equals(action.getClassPart())) {
                    TUImportationBouclementViewBean eViewBean = (TUImportationBouclementViewBean) viewBean;
                    TUImportationBouclementProcess process = new TUImportationBouclementProcess(eViewBean.getAnnee(),
                            eViewBean.getMois());
                    process.setSession((BSession) session);
                    process.setEMailAddress(eViewBean.getEMail());
                    process.setCsApplication(eViewBean.getCsApplication());
                    // process.setCsAgence(GlobazSystem.getApplication(TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(TUApplication.CS_AGENCE));
                    BSession sessionTmp = new BSession();
                    sessionTmp.setApplication(TUApplication.DEFAULT_APPLICATION_TUCANA);
                    process.setCsAgence(sessionTmp.getApplication().getProperty(TUApplication.CS_AGENCE));
                    // process.executeProcess();
                    BProcessLauncher.start(process);
                    // process.start();
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
