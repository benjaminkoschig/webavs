package globaz.lynx.helpers.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class LXExtourneHelper extends FWHelper {

    /**
     * Constructeur LXExtourneHelper.
     */
    public LXExtourneHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_delete(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        LXExtourneDelete.deleteExtourne(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        LXExtourneRetrieve.retrieveExtourne(session, viewBean);
    }
}
