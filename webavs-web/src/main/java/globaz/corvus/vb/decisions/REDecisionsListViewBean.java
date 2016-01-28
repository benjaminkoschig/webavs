/*
 * Créé le 26 juil. 07
 */
package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.globall.db.BEntity;

/**
 * @author SCR
 * 
 */
public class REDecisionsListViewBean extends REDecisionsManager {

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
        return new REDecisionsViewBean();
    }

}
