/*
 * Créé le 15.09.2006
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APListePrestationVerseeProcess;
import globaz.apg.vb.process.APListePrestationVerseeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * 
 * @author mmo
 */
public class APListePrestationVerseeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APListePrestationVerseeViewBean vb = (APListePrestationVerseeViewBean) viewBean;

        APListePrestationVerseeProcess process = new APListePrestationVerseeProcess((BSession) session);

        process.setNumeroAffilie(vb.getNumeroAffilie());
        process.setSelecteurPrestation(vb.getSelecteurPrestation());
        process.setDateDebut(vb.getDateDebut());
        process.setDateFin(vb.getDateFin());
        process.setEnvoyerGed(vb.getEnvoyerGed());
        process.setEMailAddress(vb.geteMailAddress());

        process.start();
    }
}
