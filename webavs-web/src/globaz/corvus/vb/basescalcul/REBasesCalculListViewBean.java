/*
 * Créé le 12 fevr. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.globall.db.BEntity;

/**
 * @author bsc
 * 
 */

public class REBasesCalculListViewBean extends REBasesCalculManager {

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
        return new REBasesCalculViewBean();
    }

}
