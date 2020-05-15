package globaz.apg.vb.droits;

import globaz.apg.db.droits.APEnfantMatManager;
import globaz.apg.db.droits.APEnfantPanManager;
import globaz.apg.db.droits.APSituationFamilialePanManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 *
 *
 */
public class APEnfantPanListViewBean extends APSituationFamilialePanManager implements FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APEnfantPanViewBean();
    }
}
