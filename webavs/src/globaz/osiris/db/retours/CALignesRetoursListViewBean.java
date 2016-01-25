package globaz.osiris.db.retours;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class CALignesRetoursListViewBean extends CALignesRetoursManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CALignesRetoursListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CALignesRetoursViewBean();
    }
}
