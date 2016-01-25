package globaz.musca.helpers.gestionJourFerie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.gestionJourFerie.FAPeriodeWeekendViewBean;
import globaz.musca.process.gestionJourFerie.FAPeriodeWeekendProcess;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class FAPeriodeWeekendHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        FAPeriodeWeekendViewBean vb = (FAPeriodeWeekendViewBean) viewBean;

        try {
            FAPeriodeWeekendProcess process = new FAPeriodeWeekendProcess();
            process.setSession((BSession) session);
            process.setDateDebut(vb.getDateDebut());
            process.setDateFin(vb.getDateFin());
            process.setLibelle(vb.getLibelle());
            process.setDomaineFerie(vb.getDomaineFerie());
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
