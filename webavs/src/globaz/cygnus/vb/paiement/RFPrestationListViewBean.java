/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.vb.paiement;

import globaz.cygnus.db.paiement.RFPrestationJointTiersManager;
import globaz.globall.db.BEntity;

/**
 * @author FHA
 * 
 */
public class RFPrestationListViewBean extends RFPrestationJointTiersManager {

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
        return new RFPrestationViewBean();
    }

}
