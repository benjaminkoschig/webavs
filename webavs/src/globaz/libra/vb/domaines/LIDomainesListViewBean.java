package globaz.libra.vb.domaines;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.libra.db.domaines.LIDomainesManager;

/**
 * 
 * @author HPE
 * 
 */
public class LIDomainesListViewBean extends LIDomainesManager implements FWViewBeanInterface {

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
        return new LIDomainesViewBean();
    }

}
