/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.vb.decisions;

import globaz.cygnus.db.decisions.RFDecisionJointTiersManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * 
 * @author fha
 */
public class RFDecisionJointTiersListViewBean extends RFDecisionJointTiersManager implements FWViewBeanInterface {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDecisionJointTiersViewBean();
    }

}
