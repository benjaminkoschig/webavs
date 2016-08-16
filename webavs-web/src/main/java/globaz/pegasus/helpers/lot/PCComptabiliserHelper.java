package globaz.pegasus.helpers.lot;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.lot.PCComptabiliserProcess;
import globaz.pegasus.vb.lot.PCComptabiliserViewBean;

public class PCComptabiliserHelper extends PegasusHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PCComptabiliserViewBean) {
            PCComptabiliserProcess process = new PCComptabiliserProcess();
            PCComptabiliserViewBean vb = ((PCComptabiliserViewBean) viewBean);
            process.setIdLot(vb.getIdLot());
            process.setDateComptable(vb.getDateValeurComptable());
            process.setAdresseMail(vb.geteMailAddress());
            process.setIdOrganeExecution(vb.getIdOrganeExecution());
            process.setNumeroOG(vb.getNumeroOG());
            process.setDateEcheancePaiement(vb.getDateEcheancePaiement());
            process.setSession((BSession) session);
            process.setIsoCsTypeAvis(vb.getIsoCsTypeAvis());
            process.setIsoGestionnaire(vb.getIsoGestionnaire());
            process.setIsoHightPriority(vb.getIsoHightPriority());

            try {
                process.checkMandatoryParams((BSession) session);
                BProcessLauncher.startJob(process);

            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }

        } else {
            super._start(viewBean, action, session);
        }

    }

}
