/*
 * Créé le 18 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.globall.db.BEntity;

/**
 * @author hpe
 * 
 */

public class REPeriodeInvaliditeListViewBean extends REPeriodeInvaliditeManager {

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
        return new REPeriodeInvaliditeViewBean();
    }

}
