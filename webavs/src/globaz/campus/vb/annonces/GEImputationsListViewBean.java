package globaz.campus.vb.annonces;

import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class GEImputationsListViewBean extends GEAnnoncesManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GEImputationsListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEImputationsViewBean();
    }
}
