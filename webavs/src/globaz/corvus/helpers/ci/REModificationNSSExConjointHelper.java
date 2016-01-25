/*
 * Créé le 05 sept. 07
 */
package globaz.corvus.helpers.ci;

import globaz.commons.nss.NSUtil;
import globaz.corvus.process.ci.REModificationNSSExConjointProcess;
import globaz.corvus.vb.ci.REModificationNSSExConjointViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author mmo
 * 
 */
public class REModificationNSSExConjointHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REModificationNSSExConjointViewBean vb = (REModificationNSSExConjointViewBean) viewBean;

        try {
            REModificationNSSExConjointProcess process = new REModificationNSSExConjointProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(vb.geteMailAddress());
            process.setAnneeDebut(vb.getAnneeDebut());
            process.setAnneeFin(vb.getAnneeFin());
            process.setIdRassemblement(vb.getForIdRCI());
            process.setNouveauNSS(NSUtil.unFormatAVS(vb.getNouveauNSS()));

            // processus lancé en interactif
            process.executeProcess();

        } catch (Exception e) {
            // Nothing todo
            // Tout est géré par la manière dont _validate et _executeProcess() de REModificationNSSExConjointProcess
            // ont été implémentées
            // Pas de commit de la transcation et messages remontés à l'utilisateur par FWHelper.afterExecute
        }

    }
}
