/*
 * Créé le 8 août 07
 */
package globaz.corvus.vb.taux;

import globaz.corvus.db.taux.RETauxManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class RETauxListViewBean extends RETauxManager {

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
        return new RETauxViewBean();
    }

}
