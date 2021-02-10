package globaz.apg.helpers.process;

import globaz.apg.itext.APAbstractListeRecapitulationAnnonces;
import globaz.apg.itext.APListeRecapitulationAnnonces;
import globaz.apg.itext.APListeRecapitulationAnnoncesHermes;
import globaz.apg.vb.process.APRecapitulationAnnonceViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author VRE
 * @author LGA
 * @author PBA
 */
public class APRecapitulationAnnonceHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APRecapitulationAnnonceViewBean raViewBean = (APRecapitulationAnnonceViewBean) viewBean;

        try {
            APAbstractListeRecapitulationAnnonces process = null;

            // A partir du mois de septembre 2012 une nouvelle forme d'annonce est utilisé.
            // Du coup on switch entre l'ancien et le nouveau système en fonction de la date de la récapitulation.
            String triggerDate = JadeDateUtil.getFirstDateOfMonth("08.2012");
            String dateRecapitilatif = JadeDateUtil.getFirstDateOfMonth(raViewBean.getForMoisAnneeComptable());

            if (JadeDateUtil.isDateAfter(dateRecapitilatif, triggerDate)) {
                process = new APListeRecapitulationAnnonces((BSession) session);
            } else {
                // ancien processus
                process = new APListeRecapitulationAnnoncesHermes((BSession) session);
            }

            process.setEMailAddress(raViewBean.getEMailAddress());
            process.setForMoisAnneeComptable(raViewBean.getForMoisAnneeComptable());
            if(!JadeStringUtil.isBlankOrZero(raViewBean.getForTypeAPG())){
                process.setForTypeAPG(raViewBean.getForTypeAPG());
            }
            process.start();

        } catch (FWIException e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
