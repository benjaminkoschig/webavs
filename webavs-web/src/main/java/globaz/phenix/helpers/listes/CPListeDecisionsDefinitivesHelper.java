package globaz.phenix.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.phenix.db.listes.CPListeDecisionsDefinitivesViewBean;
import globaz.phenix.process.CPListeDecisionsDefinitivesProcess;

public class CPListeDecisionsDefinitivesHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CPListeDecisionsDefinitivesViewBean vb = (CPListeDecisionsDefinitivesViewBean) viewBean;

        try {

            CPListeDecisionsDefinitivesProcess process = new CPListeDecisionsDefinitivesProcess();

            process.setSession((BSession) session);
            process.setAnnee(vb.getAnnee());
            process.setDateDebut(vb.getDateDebut());
            process.setDateFin(vb.getDateFin());
            process.setDecisionActive(vb.getDecisionActive());
            process.setFromAffilie(vb.getFromAffilie());
            process.setToAffilie(vb.getToAffilie());
            process.setTypeDecision(vb.getTypeDecision());
            process.setGenreDecision(vb.getGenreDecision());
            process.setEMailAddress(vb.getEmail());

            CPListeDecisionsDefinitivesProcess.validateData((BSession) session, vb.getDateDebut(), vb.getDateFin(),
                    vb.getAnnee());

            if (!((BSession) session).hasErrors()) {
                BProcessLauncher.start(process);
            }

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

    }

}
