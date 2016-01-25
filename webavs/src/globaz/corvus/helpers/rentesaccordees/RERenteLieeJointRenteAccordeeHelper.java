/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.utils.REPostItsFilteringUtils;
import globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeViewBean;
import globaz.framework.controller.FWAction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.prestation.helpers.PRHybridHelper;

/**
 * @author HPE
 * 
 */

public class RERenteLieeJointRenteAccordeeHelper extends PRHybridHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        super._find(persistentList, action, session);

        REPostItsFilteringUtils
                .keepPostItsForLastRenteLieeOnly((Iterable<RERenteLieeJointRenteAccordeeViewBean>) persistentList);
    }
}
