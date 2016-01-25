package globaz.al.helpers.prestation;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.recapitulatifsEntreprises.ALRecapitulatifEntreprisesImprimerProcess;
import globaz.al.vb.prestation.ALRecapViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstPrestations;

/**
 * Helper dédié au viewBean ALRecapViewBean
 * 
 * @author GMO
 * 
 */
public class ALRecapHelper extends ALAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // Impression récap unitaire
        if (viewBean instanceof ALRecapViewBean) {
            try {

                ALRecapitulatifEntreprisesImprimerProcess process = new ALRecapitulatifEntreprisesImprimerProcess();

                process.setSession((BSession) session);

                if (((ALRecapViewBean) viewBean).getRecapModel().isNew()) {
                    ((ALRecapViewBean) viewBean).retrieve();
                }
                process.setIdRecap(((ALRecapViewBean) viewBean).getRecapModel().getId());
                process.setTypeTraitRecapImpr(ALConstPrestations.TRAITEMENT_NO_RECAP);
                process.setEnvoiGED(((ALRecapViewBean) viewBean).isPrintGed());
                process.setDateImpression(JadeDateUtil.getGlobazFormattedDate(new Date()));

                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

    }

}
