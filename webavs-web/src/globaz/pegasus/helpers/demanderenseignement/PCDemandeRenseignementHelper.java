package globaz.pegasus.helpers.demanderenseignement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.demanderenseignement.PCDemandeRenseignementProcess;
import globaz.pegasus.vb.demanderenseignement.PCDemandeRenseignementViewBean;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement.DemandeRenseignementAgenceCommunaleAVSBuilder;

public class PCDemandeRenseignementHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PCDemandeRenseignementViewBean) {
            PCDemandeRenseignementViewBean vb = (PCDemandeRenseignementViewBean) viewBean;

            if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                PCDemandeRenseignementProcess process = new PCDemandeRenseignementProcess();
                process.setSession((BSession) session);

                process.setIdDemandePc(vb.getIdDemandePc());
                process.setIdGestionnaire(vb.getIdGestionnaire());
                process.setEmail(vb.getMailAddress());

                // récupération de la référence
                Map<String, List<String>> parameters = process.getBuilderParameters();
                // ajout annexes et copies
                parameters.put(DemandeRenseignementAgenceCommunaleAVSBuilder.ANNEXES, vb.getAnnexes());
                parameters.put(DemandeRenseignementAgenceCommunaleAVSBuilder.COPIES, vb.getCopies());

                parameters.put(DemandeRenseignementAgenceCommunaleAVSBuilder.ZONES_TEXTE_LIBRE,
                        Arrays.asList(vb.getZoneTexteLibre().split("\r\n")));

                try {
                    BProcessLauncher.startJob(process);
                } catch (Exception e) {
                    putTransactionInError(viewBean, e);
                }
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
