package globaz.orion.helpers.dan;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.orion.vb.dan.EBDanPreRemplissageMasseViewBean;

/**
 * Helper de lancement du process de pré-remplissage des declarations de salaire
 * 
 * @author SCO
 * @since 05 avr. 2011
 */
public class EBDanPreRemplissageMasseHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            EBDanPreRemplissageMasseViewBean vb = (EBDanPreRemplissageMasseViewBean) viewBean;

            vb.validate();

            if (!vb.getSession().hasErrors()) {
                EBDanPreRemplissage process = new EBDanPreRemplissage();
                process.setSession((BSession) session);
                process.setAnnee(vb.getAnnee());
                process.setTypeDeclaration(vb.getTypeDeclaration());
                process.setEmail(vb.getEmail());

                // Lancement du processus.
                BProcessLauncher.start(process, false);
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }

    }
}
