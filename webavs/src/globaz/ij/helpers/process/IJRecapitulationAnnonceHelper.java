/*
 * Créé le 4 septembre 2006
 */

package globaz.ij.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.itext.IJListeRecapitulationAnnonces;
import globaz.ij.vb.process.IJRecapitulationAnnonceViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */

public class IJRecapitulationAnnonceHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        IJRecapitulationAnnonceViewBean raViewBean = (IJRecapitulationAnnonceViewBean) viewBean;

        try {
            IJListeRecapitulationAnnonces process = new IJListeRecapitulationAnnonces((BSession) session);

            process.setEMailAddress(raViewBean.getEMailAddress());
            process.setForMoisAnneeComptable(raViewBean.getForMoisAnneeComptable());
            process.start();
        } catch (FWIException e) {
            e.printStackTrace();
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}