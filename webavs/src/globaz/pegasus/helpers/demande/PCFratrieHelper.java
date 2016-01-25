package globaz.pegasus.helpers.demande;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BIPersistentObject;

public class PCFratrieHelper extends FWHelper {
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) viewBean).retrieve();
    }
}
