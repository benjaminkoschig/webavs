package globaz.corvus.helpers.process;

import globaz.corvus.itext.REListeDecisionsValidees;
import globaz.corvus.vb.process.REGenererListeDecisionsValideesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class REGenererListeDecisionsValideesHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        BSession bSession = (BSession) session;
        REGenererListeDecisionsValideesViewBean gldvViewBean = (REGenererListeDecisionsValideesViewBean) viewBean;

        if (!JadeDateUtil.isGlobazDate(gldvViewBean.getDateDepuis())) {
            bSession.addError(bSession.getLabel("JSP_LDV_D_ERREUR_DATE_DEBUT"));
        }

        if (!JadeDateUtil.isGlobazDate(gldvViewBean.getDateJusqua())) {
            bSession.addError(bSession.getLabel("JSP_LDV_D_ERREUR_DATE_FIN"));
        }

        if (!bSession.hasErrors()
                && JadeDateUtil.isDateAfter(gldvViewBean.getDateDepuis(), gldvViewBean.getDateJusqua())) {
            bSession.addError(bSession.getLabel("JSP_LDV_D_ERREUR_DATES_INVALIDES"));
        }

        if (!bSession.hasErrors()) {
            REListeDecisionsValidees process = new REListeDecisionsValidees((BSession) session);
            process.setEMailAddress(gldvViewBean.getEMailAddress());
            process.setForDepuis(gldvViewBean.getDateDepuis());
            process.setForJusqua(gldvViewBean.getDateJusqua());
            process.start();
        }
    }
}