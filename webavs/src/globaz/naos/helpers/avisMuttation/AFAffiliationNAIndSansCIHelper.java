/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.avisMuttation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliationNAIndSansCIViewBean;
import globaz.naos.process.AFAffiliationNAIndSansCIProcess;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAffiliationNAIndSansCIHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIHelper.
     */
    public AFAffiliationNAIndSansCIHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFAffiliationNAIndSansCIViewBean anViewBean = (AFAffiliationNAIndSansCIViewBean) viewBean;

        try {
            AFAffiliationNAIndSansCIProcess process = new AFAffiliationNAIndSansCIProcess((BSession) session);

            process.setEMailAddress(anViewBean.getEmail());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            anViewBean.setMessage(e.getMessage());
            anViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
