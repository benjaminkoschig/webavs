package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CITransfertAffilieViewBean;
import globaz.pavo.process.CITransfertAffiliesProcess;

/**
 * 
 */
public class CITransfertAffilieHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CITransfertAffilieViewBean vb = (CITransfertAffilieViewBean) viewBean;
        CITransfertAffiliesProcess process = new CITransfertAffiliesProcess();

        try {
            process.setSession((BSession) session);
            process.setEMailAddress(vb.getEmailAddress());
            process.setNumeroAffilieDst(vb.getEmployeurDst());
            process.setNumeroAffilieSrc(vb.getEmployeurSrc());
            process.setDateDeFin(vb.getDateFusion());
            process.setImprimerAttestation(vb.isImprimerAttestations());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
            vb.setMessage(e.toString());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
