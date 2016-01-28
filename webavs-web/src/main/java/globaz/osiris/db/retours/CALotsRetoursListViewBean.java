package globaz.osiris.db.retours;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class CALotsRetoursListViewBean extends CALotsRetoursManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CALotsRetoursListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CALotsRetoursViewBean();
    }
}
