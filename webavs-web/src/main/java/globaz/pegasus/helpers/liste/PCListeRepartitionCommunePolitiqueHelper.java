package globaz.pegasus.helpers.liste;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.liste.PCListeRecapTotauxPcRfmParCommunePolitiqueProcess;
import globaz.pegasus.process.liste.PCListeRepartitionCommunePolitiqueProcess;
import globaz.pegasus.vb.liste.PCListeRepartitionCommunePolitiqueViewBean;

public class PCListeRepartitionCommunePolitiqueHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        PCListeRepartitionCommunePolitiqueViewBean vb = (PCListeRepartitionCommunePolitiqueViewBean) viewBean;

        if ("totaux".equals(vb.getTypeListe())) {

            PCListeRecapTotauxPcRfmParCommunePolitiqueProcess process = new PCListeRecapTotauxPcRfmParCommunePolitiqueProcess();

            process.setEmail(vb.getEmail());
            process.setSession((BSession) session);
            process.setDateMonthDebut(vb.getDateMonthDebut());
            process.setDateMonthFin(vb.getDateMonthFin());

            try {
                process.checkValideArguments();
            } catch (IllegalArgumentException e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);

                return;
            }

            try {
                BProcessLauncher.startJob(process);

            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }

        }

        if (PCListeRepartitionCommunePolitiqueProcess.TYPE_LISTE_PC.equals(vb.getTypeListe())
                || PCListeRepartitionCommunePolitiqueProcess.TYPE_LISTE_PC_RENTE.equals(vb.getTypeListe())) {

            PCListeRepartitionCommunePolitiqueProcess process = new PCListeRepartitionCommunePolitiqueProcess();
            process.setTypeListe(vb.getTypeListe());
            process.setEmail(vb.getEmail());
            process.setSession((BSession) session);

            try {
                BProcessLauncher.startJob(process);

            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }

        }

    }
}
