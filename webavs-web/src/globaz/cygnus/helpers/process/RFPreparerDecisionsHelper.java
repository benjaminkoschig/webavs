/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFPreparerDecisionsProcess;
import globaz.cygnus.vb.process.RFPreparerDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFPreparerDecisionsHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        RFPreparerDecisionsViewBean vb = (RFPreparerDecisionsViewBean) viewBean;

        RFPreparerDecisionsProcess process = new RFPreparerDecisionsProcess();
        process.setSession((BSession) session);

        process.setEMailAddress(vb.getEMailAddress());
        process.setDateSurDocument(vb.getDateSurDocument());
        process.setIdGestionnaire(vb.getIdGestionnaire());

        process.start();
    }
}
