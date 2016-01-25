package globaz.lynx.db.informationcomptable;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXInformationComptableListViewBean extends LXInformationComptableManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXInformationComptableListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXInformationComptableViewBean();
    }
}
