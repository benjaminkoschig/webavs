package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.process.CIAnnonceCentraleGenerationViewBean;
import globaz.pavo.process.CIAnnonceCentraleGenerationProcess;

/**
 * 
 * @author: mmo
 */
public class CIAnnonceCentraleGenerationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {

            if (CIAnnonceCentraleGenerationViewBean.class.isAssignableFrom(viewBean.getClass())) {
                CIAnnonceCentraleGenerationViewBean annonceCentraleGenerationViewBean = (CIAnnonceCentraleGenerationViewBean) viewBean;

                CIAnnonceCentrale annonceCentrale = new CIAnnonceCentrale();
                annonceCentrale.setSession((BSession) session);
                annonceCentrale.setAnnonceCentraleId(annonceCentraleGenerationViewBean.getIdAnnonceCentrale());
                annonceCentrale.retrieve();

                CIAnnonceCentraleGenerationProcess annonceCentraleGenerationProcess = new CIAnnonceCentraleGenerationProcess();
                annonceCentraleGenerationProcess.setSession((BSession) session);
                annonceCentraleGenerationProcess.setEMailAddress(annonceCentraleGenerationViewBean.getEmailAddress());
                annonceCentraleGenerationProcess
                        .setModeLancement(CIAnnonceCentraleGenerationProcess.MODE_LANCEMENT_MANUEL);
                annonceCentraleGenerationProcess.setAnnonceCentrale(annonceCentrale);

                BProcessLauncher.start(annonceCentraleGenerationProcess);
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

    }

}
