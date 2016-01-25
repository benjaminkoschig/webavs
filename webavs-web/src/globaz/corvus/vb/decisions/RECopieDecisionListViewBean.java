/*
 * Créé le 20 août 07
 */
package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.RECopieDecisionManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class RECopieDecisionListViewBean extends RECopieDecisionManager {

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
        return new RECopieDecisionViewBean();
    }

}
