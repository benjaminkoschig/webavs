package globaz.perseus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.decision.PFDecisionRefusFactureProcess;
import globaz.perseus.vb.decision.PFDecisionRefusFactureViewBean;
import java.util.HashMap;

public class PFDecisionRefusFactureHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (viewBean instanceof PFDecisionRefusFactureViewBean) {
            ((PFDecisionRefusFactureViewBean) viewBean).init();
        }

        super._init(viewBean, action, session);
    }

    /**
     * Méthode de démarrage du process
     * 
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // Si decision processviewbean, pour document
        if (viewBean instanceof PFDecisionRefusFactureViewBean) {
            PFDecisionRefusFactureViewBean vb = (PFDecisionRefusFactureViewBean) viewBean;
            PFDecisionRefusFactureProcess process = new PFDecisionRefusFactureProcess();
            process.setSession((BSession) session);
            process.setEmailAdresse(vb.geteMailAdresse());
            process.setIsSendToGed(vb.getIsSendToGed());

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("dateDocument", vb.getDateDocument());
            data.put("detailAssure", vb.getDetailAssure());
            data.put("eMailAdresse", vb.geteMailAdresse());
            data.put("gestionnaire", vb.getGestionnaire());
            data.put("idDossier", vb.getIdDossier());
            data.put("agenceAssurance", vb.getAgenceAssurance());
            data.put("caisse", vb.getCaisse());
            data.put("membreFamille", vb.getMembreFamille());
            data.put("texteLibre", vb.getTexteLibre());
            data.put("typeFacture", vb.getTypeFacture());
            data.put("idTiersAdresseCourrier", vb.getIdTiersAdresseCourrier());
            data.put("idDomaineApplicatifAdresseCourrier", vb.getIdDomaineApplicatifAdresseCourrier());

            process.setDonneesProcess(data);

            /**
             * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
             * doit donc être renommée différement (mailAd) pour fonctionner correctement.
             */

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
