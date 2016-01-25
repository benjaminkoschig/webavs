package globaz.perseus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.decision.PFDecisionRefusDemandeProcess;
import globaz.perseus.vb.decision.PFDecisionRefusDemandeViewBean;
import java.util.HashMap;

public class PFDecisionRefusDemandeHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (viewBean instanceof PFDecisionRefusDemandeViewBean) {
            ((PFDecisionRefusDemandeViewBean) viewBean).init();
        }

        super._init(viewBean, action, session);
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PFDecisionRefusDemandeViewBean) {

            PFDecisionRefusDemandeViewBean vb = (PFDecisionRefusDemandeViewBean) viewBean;
            PFDecisionRefusDemandeProcess process = new PFDecisionRefusDemandeProcess();
            process.setSession((BSession) session);
            process.setEmailAdresse(vb.geteMailAdresse());
            process.setIsSendToGed(vb.getIsSendToGed());

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("dateDocument", vb.getDateDocument());
            data.put("eMailAdresse", vb.geteMailAdresse());
            data.put("idDossier", vb.getIdDossier());
            data.put("gestionnaire", vb.getGestionnaire());
            data.put("idTiersAdresseCourrier", vb.getIdTiersAdresseCourrier());
            data.put("idDomaineApplicatifAdresseCourrier", vb.getIdDomaineApplicatifAdresseCourrier());
            data.put("agenceAssurance", vb.getAgenceAssurance());
            data.put("caisse", vb.getCaisse());

            process.setDonneesProcess(data);

            try {
                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                e.printStackTrace();
                viewBean.setMessage("Unable to start........");
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        } else {
            super._start(viewBean, action, session);
        }

    }

}
