package globaz.helios.helpers.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class CGGestionEcritureHelper extends FWHelper {

    /**
     * Constructeur CGExerciceComptableHelper.
     */
    public CGGestionEcritureHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CGGestionEcritureAdd.addEcritures(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_delete(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CGGestionEcritureDelete.deleteEcritures(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CGGestionEcritureRetrieve.retrieveEcritures(session, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        CGGestionEcritureUpdate.updateEcritures(session, viewBean);
    }
}
