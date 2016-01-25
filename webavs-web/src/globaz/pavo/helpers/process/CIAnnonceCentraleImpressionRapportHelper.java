package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.process.CIAnnonceCentraleImpressionRapportViewBean;
import globaz.pavo.process.CIAnnonceCentraleImpressionRapportProcess;

/**
 * 
 * @author: mmo
 */
public class CIAnnonceCentraleImpressionRapportHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {

            CIAnnonceCentraleImpressionRapportViewBean vb = (CIAnnonceCentraleImpressionRapportViewBean) viewBean;

            CIAnnonceCentrale theAnnonce = new CIAnnonceCentrale();
            theAnnonce.setSession((BSession) session);
            theAnnonce.setAnnonceCentraleId(vb.getIdAnnonceCentrale());
            theAnnonce.retrieve();

            CIAnnonceCentraleImpressionRapportProcess annonceCentraleImpressionRapportProcess = new CIAnnonceCentraleImpressionRapportProcess();
            annonceCentraleImpressionRapportProcess.setSession((BSession) session);
            annonceCentraleImpressionRapportProcess.setAnnonceCentrale(theAnnonce);
            annonceCentraleImpressionRapportProcess.setModeFonctionnement(vb.getModeFonctionnement());
            annonceCentraleImpressionRapportProcess.setEMailAddress(vb.getEmailAddress());

            BProcessLauncher.startJob(annonceCentraleImpressionRapportProcess);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
