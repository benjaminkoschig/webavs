package globaz.ij.vb.annonces;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.annonces.IJAnnonceHierarchiqueManager;

/**
 * @author DVH
 */
public class IJAnnonceListViewBean extends IJAnnonceHierarchiqueManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJAnnonceViewBean();
    }
}
