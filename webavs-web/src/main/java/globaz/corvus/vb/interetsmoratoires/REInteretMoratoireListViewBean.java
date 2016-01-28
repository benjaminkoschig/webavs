/*
 * Créé le 2 août 07
 */
package globaz.corvus.vb.interetsmoratoires;

import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoireManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class REInteretMoratoireListViewBean extends REInteretMoratoireManager {

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
        return new REInteretMoratoireViewBean();
    }

    @Override
    public String getOrderByDefaut() {
        return REInteretMoratoire.FIELDNAME_ID_TIERS_ADR_PMT + ", " + super.getOrderByDefaut() + " DESC";
    }
}
