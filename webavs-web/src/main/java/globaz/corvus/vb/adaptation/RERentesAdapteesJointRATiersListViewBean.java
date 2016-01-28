package globaz.corvus.vb.adaptation;

import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiersManager;
import globaz.globall.db.BEntity;

public class RERentesAdapteesJointRATiersListViewBean extends RERentesAdapteesJointRATiersManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERentesAdapteesJointRATiersViewBean();
    }

}