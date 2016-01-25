package globaz.pegasus.helpers.transfertdossier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.annonce.transfertdossier.PCDemandeTransfertRenteProcess;
import globaz.pegasus.vb.transfertdossier.PCDemandeTransfertRenteViewBean;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeTransfertRenteHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PCDemandeTransfertRenteViewBean) {
            PCDemandeTransfertRenteViewBean vb = (PCDemandeTransfertRenteViewBean) viewBean;

            try {
                // verification de tout élément pouvant interrompre le process
                PegasusServiceLocator.getTransfertRentePCProviderService()
                        .checkProcessArguments(vb.getIdCaisseAgence());
                if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                        && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    PCDemandeTransfertRenteProcess process = new PCDemandeTransfertRenteProcess();
                    process.setSession((BSession) session);

                    process.setIdDemandePc(vb.getIdDemandePc());
                    process.setIdGestionnaire(vb.getIdGestionnaire());
                    process.setEmail(vb.getMailAddress());
                    process.setIdAgence(vb.getIdCaisseAgence());
                    process.setNoAgence(vb.getNoCaisseAgence());
                    process.setDateTransfert(vb.getDateTransfert());
                    process.setDateAnnonce(vb.getDateAnnonce());

                    BProcessLauncher.startJob(process);

                }
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
