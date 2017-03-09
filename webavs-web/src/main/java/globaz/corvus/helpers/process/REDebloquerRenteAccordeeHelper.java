package globaz.corvus.helpers.process;

import globaz.corvus.vb.process.REDebloquerMontantRAViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;

public class REDebloquerRenteAccordeeHelper extends PRAbstractHelper {

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REDebloquerMontantRAViewBean vb = (REDebloquerMontantRAViewBean) viewBean;

        // try {
        // REDebloquerMontantRenteAccordeeProcess process = new REDebloquerMontantRenteAccordeeProcess();
        // process.setSession((BSession) session);
        // process.setEMailAddress(vb.getEMailAdress());
        // process.setIdRenteAccordee(vb.getIdRenteAccordee());
        //
        // process.start();
        //
        // } catch (Exception e) {
        // vb.setMsgType(FWViewBeanInterface.ERROR);
        // vb.setMessage(e.toString());
        // }
    }
}
