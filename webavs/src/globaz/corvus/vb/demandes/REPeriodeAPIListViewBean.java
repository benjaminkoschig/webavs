/*
 * Créé le 15 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.globall.db.BEntity;

/**
 * @author hpe
 * 
 */

public class REPeriodeAPIListViewBean extends REPeriodeAPIManager {

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
        return new REPeriodeAPIViewBean();
    }
}
