/*
 * Cr?? le 30 novembre 2009
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jje
 */
public class RFQdSaisieAugmentationListViewBean extends RFQdAugmentationManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdSaisieAugmentationViewBean();
    }

}