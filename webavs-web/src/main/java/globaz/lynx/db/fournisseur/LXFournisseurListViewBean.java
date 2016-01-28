package globaz.lynx.db.fournisseur;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXFournisseurListViewBean extends LXFournisseurManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXFournisseurListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXFournisseurViewBean();
    }
}
