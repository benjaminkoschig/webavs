/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.acompte.AFComparaisonAcompteMasseViewBean;
import globaz.naos.process.AFListeExcelComparaisonAcompteMasseProcess;

/**
 * @author BJO
 * 
 */
public class AFComparaisonAcompteMasseHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIHelper.
     */
    public AFComparaisonAcompteMasseHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFComparaisonAcompteMasseViewBean anViewBean = (AFComparaisonAcompteMasseViewBean) viewBean;

        try {
            AFListeExcelComparaisonAcompteMasseProcess process = new AFListeExcelComparaisonAcompteMasseProcess();
            process.setSession((BSession) session);
            process.setEMailAddress(anViewBean.getEmail());
            process.setAnneeAcompte(anViewBean.getAnneeAcompte());
            process.setAnneeMasse(anViewBean.getAnneeMasse());
            process.setDifferenceTolereeFranc(anViewBean.getDifferenceTolereeFranc());
            process.setDifferenceTolereeTaux(anViewBean.getDifferenceTolereeTaux());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            anViewBean.setMessage(e.getMessage());
            anViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
