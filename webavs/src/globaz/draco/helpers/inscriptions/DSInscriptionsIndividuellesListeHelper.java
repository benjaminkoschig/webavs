package globaz.draco.helpers.inscriptions;

import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BIPersistentObject;

public class DSInscriptionsIndividuellesListeHelper extends FWHelper {

    public DSInscriptionsIndividuellesListeHelper() {
        super();
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) viewBean).retrieve();
        ((DSInscriptionsIndividuellesListeViewBean) viewBean)._initialise();

    }

}
