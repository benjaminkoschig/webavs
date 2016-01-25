package globaz.pavo.helpers.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.inscriptions.CIDeclarationViewBean;
import globaz.pavo.process.CIDeclaration;

public class CIDeclarationHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIDeclarationViewBean vb = (CIDeclarationViewBean) viewBean;
        try {
            CIDeclaration process = new CIDeclaration(vb.getSession());
            process.setAnneeCotisation(vb.getAnneeCotisation());
            process.setFilename(vb.getFilename());
            process.setForNumeroAffilie(vb.getForNumeroAffilie());
            process.setTotalControle(vb.getTotalControle());
            process.setNombreInscriptions(vb.getNombreInscriptions());
            process.setSimulation(vb.getSimulation());
            process.setType(vb.getType());
            process.setAccepteEcrituresNegatives(vb.getAccepteEcrituresNegatives());
            process.setAccepteLienDraco(vb.getAccepteLienDraco());
            process.setAccepteAnneeEnCours(vb.getAccepteAnneeEnCours());
            process.setEMailAddress(vb.getEmailAddress());
            process.setDateReceptionForced(vb.getDateReceptionForced());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
