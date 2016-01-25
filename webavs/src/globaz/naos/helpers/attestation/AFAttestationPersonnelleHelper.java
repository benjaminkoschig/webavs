package globaz.naos.helpers.attestation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.attestation.AFAttestationPersonnelleViewBean;
import globaz.naos.process.AFAttestationPersonnelleProcess;

/**
 * 
 * @author SCO
 * @since 05 juil. 2011
 */
public class AFAttestationPersonnelleHelper extends FWHelper {

    /**
     * Constructeur de AFAttesatationPersonnelleHelper
     */
    public AFAttestationPersonnelleHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        AFAttestationPersonnelleViewBean vb = (AFAttestationPersonnelleViewBean) viewBean;

        try {

            AFAttestationPersonnelleProcess process = new AFAttestationPersonnelleProcess();
            process.setSession((BSession) session);

            process.setAnnee(vb.getAnnee());
            process.setFromAffilie(vb.getFromAffilie());
            process.setToAffilie(vb.getToAffilie());
            process.setDateEnvoiMasse(vb.getDateEnvoiMasse());
            process.setEMailAddress(vb.getEmail());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

    }
}
