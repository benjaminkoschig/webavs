package globaz.musca.helpers.gestionJourFerie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.gestionJourFerie.FAModifierSupprimerPeriodeViewBean;
import globaz.musca.process.gestionJourFerie.FAModifierSupprimerPeriodeProcess;

/**
 * @author MMO
 * @since 10 novembre 2010
 */
public class FAModifierSupprimerPeriodeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        FAModifierSupprimerPeriodeViewBean vb = (FAModifierSupprimerPeriodeViewBean) viewBean;

        try {
            FAModifierSupprimerPeriodeProcess process = new FAModifierSupprimerPeriodeProcess();
            process.setSession((BSession) session);
            process.setListFerieAModifierSupprimer(vb.getListFerieAModifierSupprimer());
            process.setOperationARealiser(vb.getOperationARealiser());
            process.setLibelle(vb.getLibelle());
            process.setDomaineFerie(vb.getDomaineFerie());
            process.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
