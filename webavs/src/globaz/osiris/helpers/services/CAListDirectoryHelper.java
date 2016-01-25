package globaz.osiris.helpers.services;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.osiris.db.services.CAListDirectoryManager;

public class CAListDirectoryHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_find(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _find(Object viewBean, FWAction action, BISession session) throws Exception {
        ((CAListDirectoryManager) viewBean).find();
    }

}
