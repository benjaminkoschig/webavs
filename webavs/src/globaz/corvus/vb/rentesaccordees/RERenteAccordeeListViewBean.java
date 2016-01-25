/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.globall.db.BEntity;

/**
 * @author bsc
 * 
 */

public class RERenteAccordeeListViewBean extends RERenteAccordeeManager {

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
        return new RERenteAccordeeViewBean();
    }

}
