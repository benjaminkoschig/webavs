package globaz.cygnus.vb.typeDeSoins;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFParametrageTypeSoinsRecherchePeriodeListViewBean extends
        RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager implements FWViewBeanInterface {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFParametrageTypeSoinsRecherchePeriodeViewBean();
    }

}
