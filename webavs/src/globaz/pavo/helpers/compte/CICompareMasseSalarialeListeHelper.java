package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompareMasseSalarialeListeViewBean;
import globaz.pavo.process.CIAbstimmfileDocument;
import globaz.pavo.process.CIAbstimmfileProcess;

public class CICompareMasseSalarialeListeHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CICompareMasseSalarialeListeViewBean vb = (CICompareMasseSalarialeListeViewBean) viewBean;
        try {
            CIAbstimmfileProcess process = new CIAbstimmfileProcess(vb.getSession());
            process.setAnneeDebut(vb.getAnneeDebut());
            process.setAnneeFin(vb.getAnneeFin());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            process.setAllAffilie(vb.getAllAffilie());
            process.setMontant(vb.getMontant());
            process.setMontantOperator(CIAbstimmfileDocument.Operator.valueOf(vb.getMontantOperator()));
            process.setNumeorTo(vb.getNumeroTo());
            process.setNumeroFrom(vb.getNumeroFrom());

            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
