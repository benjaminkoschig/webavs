package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.phenix.db.principale.CPLienTypeDecRemarqueViewBean;

public class CPLienTypeDecRemarqueHelper extends FWHelper {

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        if (viewBean instanceof CPLienTypeDecRemarqueViewBean) {
            ((CPLienTypeDecRemarqueViewBean) viewBean)._chargerEntete();
        } else {
            super._chercher(viewBean, action, session);
        }
    }
}
