/*
 * Créé le 10 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APPeriodeAPGManager;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPeriodeAPGListViewBean extends APPeriodeAPGManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.db.droits.APPeriodeAPGManager#_newEntity()
     */

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
        return new APPeriodeAPGViewBean();
    }
}
