/*
 * Créé le 06 octobre 05
 */
package globaz.ij.vb.lots;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.lots.IJFactureJointCompensationManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureJointCompensationListViewBean extends IJFactureJointCompensationManager implements
        FWListViewBeanInterface {

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
        return new IJFactureJointCompensationViewBean();
    }
}
