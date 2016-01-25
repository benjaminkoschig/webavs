package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.phenix.db.principale.CPDonneesCalculViewBean;

public class CPDonneesCalculHelper extends FWHelper {

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        if (viewBean instanceof CPDonneesCalculViewBean) {
            ((CPDonneesCalculViewBean) viewBean)._chargerEntete();
        } else {
            super._chercher(viewBean, action, session);
        }
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof CPDonneesCalculViewBean) {
            ((CPDonneesCalculViewBean) viewBean)._chargerEntete();
        } else {
            super._init(viewBean, action, session);
        }
    }
}
