// créé le 24 mars 2010
package globaz.cygnus.vb.motifsDeRefus;

import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFRechercheMotifsDeRefusListViewBean extends RFMotifsDeRefusManager implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFRechercheMotifsDeRefusViewBean();
    }

}