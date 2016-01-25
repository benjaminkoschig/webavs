package globaz.corvus.helpers.process;

import globaz.corvus.process.REGenererRenteVeuvePerdureProcess;
import globaz.corvus.vb.process.REGenererRenteVeuvePerdureViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererRenteVeuvePerdureHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // on ne fait rien, nécessaire pour que les appels à _retrieve() depuis le FW soient ignorés
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        BSession bSession = (BSession) session;

        REGenererRenteVeuvePerdureViewBean renteViewBean = (REGenererRenteVeuvePerdureViewBean) viewBean;

        REGenererRenteVeuvePerdureProcess process = new REGenererRenteVeuvePerdureProcess();
        process.setSession(bSession);
        process.setDateDocument(renteViewBean.getDateDocument());
        process.setIdDemandeRente(renteViewBean.getIdDemandeRente());
        process.setMontantRenteVeuve(renteViewBean.getMontantRenteVeuve());
        process.setMontantRenteVieillesse(renteViewBean.getMontantRenteVieillesse());
        process.setDateDebutRenteVieillesse(renteViewBean.getDateDebutRenteVieillesse());
        process.setIdTiers(renteViewBean.getIdTiers());
        process.setEmailAddress(renteViewBean.getAdresseEmail());
        process.setIsSendToGed(renteViewBean.getSendToGed());

        StringBuilder annexes = new StringBuilder();
        process.setAnnexeParDefaut(renteViewBean.isAnnexeParDefaut());
        if (!renteViewBean.getAnnexes().isEmpty()) {
            for (String unAnnexe : renteViewBean.getAnnexes()) {
                if (annexes.length() > 0) {
                    annexes.append("\n");
                }
                annexes.append(unAnnexe);
            }
        }
        process.setAnnexes(annexes.toString());

        try {
            BProcessLauncher.start(process, false);
        } catch (Exception ex) {
            bSession.addError(ex.toString());
        }
    }
}
