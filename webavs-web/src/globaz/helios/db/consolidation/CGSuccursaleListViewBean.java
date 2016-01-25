package globaz.helios.db.consolidation;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class CGSuccursaleListViewBean extends CGSuccursaleManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGSuccursaleViewBean();
    }
}
