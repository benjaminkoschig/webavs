package globaz.corvus.helpers.process;

import globaz.corvus.itext.REListeImpotSource;
import globaz.corvus.vb.process.REGenererListeImpotSourceViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.common.properties.CommonProperties;

public class REGenererListeImpotSourceHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        REGenererListeImpotSourceViewBean glisViewBean = (REGenererListeImpotSourceViewBean) viewBean;

        try {
            REListeImpotSource process = new REListeImpotSource();
            process.setSession(glisViewBean.getSession());
            process.setEMailAddress(glisViewBean.getEMailAddress());
            process.setMoisDebut(glisViewBean.getMoisDebut());
            process.setMoisFin(glisViewBean.getMoisFin());
            process.setCanton(glisViewBean.getCanton());
            process.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
            process.start();
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}