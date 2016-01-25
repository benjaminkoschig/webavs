/*
 * Créé le 17 juil. 07
 */
package globaz.corvus.vb.retenues;

import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.globall.db.BEntity;

/**
 * @author HPE
 * 
 */
public class RERetenuesPaiementListViewBean extends RERetenuesPaiementManager {

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
        return new RERetenuesPaiementViewBean();
    }

}
