/*
 * Créé le 6 juin 05
 */
package globaz.apg.vb.lots;

import globaz.apg.db.lots.APLotManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author dvh
 * 
 */
public class APLotListViewBean extends APLotManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new APLotViewBean();
    }

}
