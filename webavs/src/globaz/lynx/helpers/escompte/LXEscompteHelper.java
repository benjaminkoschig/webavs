package globaz.lynx.helpers.escompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class LXEscompteHelper extends FWHelper {

    /**
     * Constructeur LXPaiementHelper.
     */
    public LXEscompteHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_delete(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        LXEscompteDelete.deleteEscompte(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        LXEscompteRetrieve.retrieveEscompte(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        LXEscompteUpdate.updateEscompte(session, viewBean);
    }
}
