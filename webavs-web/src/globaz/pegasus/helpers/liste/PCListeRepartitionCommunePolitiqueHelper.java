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

            process.setISession(session);
            process.setEMailAddress(vb.getEmail());
            process.setDateMonthDebut(vb.getDateMonthDebut());
            process.setDateMonthFin(vb.getDateMonthFin());

            try {
                BProcessLauncher.start(process);

            } catch (Exception e) {
                vb.setMessage(e.toString());
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

        }

        if ("listePc".equals(vb.getTypeListe()) || "listePcRente".equals(vb.getTypeListe())) {

            try {
                PCListeRepartitionCommunePolitiqueProcess process = new PCListeRepartitionCommunePolitiqueProcess();
                process.setTypeListe(vb.getTypeListe());
                process.setEmail(vb.getEmail());
                process.setSession((BSession) session);
                BProcessLauncher.startJob(process);

            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }

        }

    }
}
