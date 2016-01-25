package globaz.perseus.helpers.lot;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.perseus.process.decision.PFImprimerDecisionFactureProcess;
import globaz.perseus.utils.PFUserHelper;
import globaz.perseus.vb.lot.PFImprimerDecisionFactureViewBean;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.web.application.PFApplication;

public class PFImprimerDecisionFactureHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._init(viewBean, action, session);
        if (viewBean instanceof PFImprimerDecisionFactureViewBean) {
            ((PFImprimerDecisionFactureViewBean) viewBean).init();
        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PFImprimerDecisionFactureViewBean) {
            try {
                PFImprimerDecisionFactureProcess process = new PFImprimerDecisionFactureProcess();
                process.setSession((BSession) session);
                /**
                 * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et
                 * doit donc être renommée différement (mailAd) pour fonctionner correctement.
                 */
                process.setAdrMail(((PFImprimerDecisionFactureViewBean) viewBean).getAdrMail());
                process.setDateDocument(((PFImprimerDecisionFactureViewBean) viewBean).getDateDocument());
                process.setIdLot(((PFImprimerDecisionFactureViewBean) viewBean).getIdLot());
                process.setCaisse(((PFImprimerDecisionFactureViewBean) viewBean).getCaisse());
                process.setIsSendToGed(((PFImprimerDecisionFactureViewBean) viewBean).getIsSendToGed());

                if ((process.getCaisse().equals(CSCaisse.CCVD.getCodeSystem()))
                        || (process.getCaisse().equals(CSCaisse.AGENCE_LAUSANNE.getCodeSystem()))) {

                    List<String> listAgenceComplete = new ArrayList<String>();
                    for (String key : PFUserHelper.getUsermap(PFApplication.DEFAULT_APPLICATION_PERSEUS,
                            PFApplication.PROPERTY_GROUPE_PERSEUS_AGENCE).keySet()) {
                        listAgenceComplete.add(key);
                    }
                    process.setIdUserAgence(listAgenceComplete);
                } else {
                    process.setIsAgence(true);
                    process.setIdUserAgence(((PFImprimerDecisionFactureViewBean) viewBean).getAgencesSelectionne());
                }

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
