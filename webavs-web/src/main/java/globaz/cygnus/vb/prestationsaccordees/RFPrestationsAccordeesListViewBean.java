package globaz.cygnus.vb.prestationsaccordees;

import globaz.cygnus.db.paiement.RFPrestationAccordeeJointTiersManager;
import globaz.globall.db.BEntity;

/**
 * @author fha
 */
public class RFPrestationsAccordeesListViewBean extends RFPrestationAccordeeJointTiersManager {

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
        return new RFPrestationsAccordeesViewBean();
    }
}
