/*
 * Créé le 15.09.2006
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APListePrestationCIABProcess;
import globaz.apg.vb.process.APListePrestationCIABViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * 
 * @author eniv
 */
public class APListePrestationCIABHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APListePrestationCIABViewBean vb = (APListePrestationCIABViewBean) viewBean;

        APListePrestationCIABProcess process = new APListePrestationCIABProcess((BSession) session);

        process.setSelecteurPrestation(vb.getSelecteurPrestation());
        process.setDateDebut(vb.getDateDebut());
        process.setDateFin(vb.getDateFin());
        process.setEnvoyerGed(vb.getEnvoyerGed());
        process.setEMailAddress(vb.geteMailAddress());

        process.start();
    }
}
