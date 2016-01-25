/*
 * Créé le 25 juin. 07
 */
package globaz.corvus.vb.ci;

import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.globall.db.BEntity;

/**
 * @author bsc
 * 
 */

public class RERassemblementCIListViewBean extends RERassemblementCIManager {

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
        return new RERassemblementCIViewBean();
    }

    @Override
    public String getOrderByDefaut() {
        return RERassemblementCI.FIELDNAME_ID_RCI + " DESC";
    }

}
