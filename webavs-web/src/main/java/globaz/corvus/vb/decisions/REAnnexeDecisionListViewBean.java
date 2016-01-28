/*
 * Créé le 20 août 07
 */
package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REAnnexeDecisionManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class REAnnexeDecisionListViewBean extends REAnnexeDecisionManager {

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
        return new REAnnexeDecisionViewBean();
    }

}
