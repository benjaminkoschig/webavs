package globaz.campus.vb.etudiants;

import globaz.campus.db.etudiants.GEEtudiantsTiersManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class GEEtudiantsListViewBean extends GEEtudiantsTiersManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GEEtudiantsListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEEtudiantsViewBean();
    }
}
