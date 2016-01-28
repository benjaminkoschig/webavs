package globaz.campus.vb.annonces;

import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class GEAnnoncesListViewBean extends GEAnnoncesManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GEAnnoncesListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEAnnoncesViewBean();
    }
}
