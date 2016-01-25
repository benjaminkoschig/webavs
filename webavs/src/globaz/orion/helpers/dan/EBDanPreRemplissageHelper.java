package globaz.orion.helpers.dan;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.orion.vb.dan.EBDanPreRemplissageViewBean;

/**
 * Helper de lancement du process de pr�-remplissage de la d�claration de salaire d'un affili�
 * 
 * @author SCO
 * @since 05 avr. 2011
 */
public class EBDanPreRemplissageHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            EBDanPreRemplissageViewBean vb = (EBDanPreRemplissageViewBean) viewBean;

            vb.validate();

            if (!vb.getSession().hasErrors()) {
                EBDanPreRemplissage process = new EBDanPreRemplissage();
                process.setSession((BSession) session);
                process.setAnnee(vb.getAnnee());
                process.setNumAffilie(vb.getNumAffilie());
                process.setEmail(vb.getEmail());

                // Lancement du processus.
                BProcessLauncher.start(process, false);
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }

    }
}
