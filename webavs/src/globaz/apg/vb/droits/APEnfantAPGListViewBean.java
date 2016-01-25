/*
 * Créé le 27 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APEnfantAPGJointTiersManager;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class APEnfantAPGListViewBean extends APEnfantAPGJointTiersManager {

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
        return new APEnfantAPGViewBean();
    }
}
