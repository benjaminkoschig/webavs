package globaz.phenix.helpers.principale;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BIPersistentObjectList;
import globaz.phenix.db.principale.CPDecisionListViewBean;

public class CPDecisionSuiviHelper extends FWHelper {

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (persistentList instanceof CPDecisionListViewBean) {
            persistentList.find();
        } else {
            super._find(persistentList, action, session);
        }

    }
}
