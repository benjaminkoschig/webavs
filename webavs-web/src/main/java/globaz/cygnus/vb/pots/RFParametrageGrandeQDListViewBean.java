package globaz.cygnus.vb.pots;

import globaz.cygnus.db.pots.RFPotsPCManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFParametrageGrandeQDListViewBean extends RFPotsPCManager implements FWViewBeanInterface {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFParametrageGrandeQDViewBean();
    }

}
