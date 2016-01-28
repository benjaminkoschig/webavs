package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.process.CIImprimerInscriptionActifViewBean;
import globaz.pavo.print.itext.CIImpressionListeActif;

public class CIImprimerInscriptionActifHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIImprimerInscriptionActifViewBean vb = (CIImprimerInscriptionActifViewBean) viewBean;
        try {
            CIImpressionListeActif process = new CIImpressionListeActif(vb.getSession());

            process.setForAnnee(vb.getDateDebut());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
