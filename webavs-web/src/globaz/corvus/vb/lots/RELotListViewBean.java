/*
 * Créé le 26 juil. 07
 */
package globaz.corvus.vb.lots;

import globaz.corvus.db.lots.RELotManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class RELotListViewBean extends RELotManager {

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
        return new RELotViewBean();
    }

}
