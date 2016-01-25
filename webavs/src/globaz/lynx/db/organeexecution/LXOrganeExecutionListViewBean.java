package globaz.lynx.db.organeexecution;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXOrganeExecutionListViewBean extends LXOrganeExecutionManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXOrganeExecutionListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOrganeExecutionViewBean();
    }
}
