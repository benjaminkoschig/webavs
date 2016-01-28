/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.statOfas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.statOfas.AFStatOFASControleViewBean;
import globaz.naos.itext.statOfas.AFStatOFASControle_Doc;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFStatOFASControleHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIHelper.
     */
    public AFStatOFASControleHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            AFStatOFASControleViewBean asViewBean = (AFStatOFASControleViewBean) viewBean;
            AFStatOFASControle_Doc document = new AFStatOFASControle_Doc();

            document.setAnnee(asViewBean.getAnnee());
            document.setEMailAddress(asViewBean.getEmail());
            document.setISession(session);
            BProcessLauncher.start(document);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }

}
