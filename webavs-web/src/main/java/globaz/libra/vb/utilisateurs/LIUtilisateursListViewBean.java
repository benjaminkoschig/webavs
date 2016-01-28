package globaz.libra.vb.utilisateurs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;

public class LIUtilisateursListViewBean extends LIUtilisateursManager implements FWViewBeanInterface {

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
        return new LIUtilisateursViewBean();
    }

}