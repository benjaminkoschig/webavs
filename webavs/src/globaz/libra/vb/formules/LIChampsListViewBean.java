package globaz.libra.vb.formules;

import globaz.envoi.db.parametreEnvoi.access.ENChampManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

public class LIChampsListViewBean extends ENChampManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIChampsViewBean();
    }

}