/*
 * Créé le 8 sept. 05
 */
package globaz.ij.vb.lots;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.lots.IJLotManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJLotListViewBean extends IJLotManager implements FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJLotViewBean();
    }
}
