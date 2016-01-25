/*
 * Créé le 27 juil. 07
 */
package globaz.cygnus.vb.ordresversements;

import globaz.cygnus.db.ordresversements.RFOrdresVersementsManager;
import globaz.globall.db.BEntity;

/**
 * @author HPE
 * 
 */
public class RFOrdresVersementsListViewBean extends RFOrdresVersementsManager {

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
        return new RFOrdresVersementsViewBean();
    }
}
