package globaz.lynx.db.ordregroupe;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXOrdreGroupeListViewBean extends LXOrdreGroupeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXOrdreGroupeListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOrdreGroupeViewBean();
    }
}
