/*
 * Cr�� le 15 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.prestation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.application.TIApplication;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APAdressePaiementHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.IFWHelper#beforeExecute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super.beforeExecute(viewBean, action, session);
        viewBean.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
    }
}
