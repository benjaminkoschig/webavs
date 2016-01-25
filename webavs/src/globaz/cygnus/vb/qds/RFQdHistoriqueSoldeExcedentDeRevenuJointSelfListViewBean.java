/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFQdHistoriqueSoldeExcedentDeRevenuJointSelfManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean extends
        RFQdHistoriqueSoldeExcedentDeRevenuJointSelfManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean loadHistorique(BSession session,
            String idQd) throws Exception {

        RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean retValue = null;

        if (!JadeStringUtil.isNull(idQd)) {
            retValue = new RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean();
            retValue.setForIdQd(idQd);
            retValue.setSession(session);
            retValue.changeManagerSize(0);
            retValue.find();
        }

        return retValue;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdHistoriqueSoldeExcedentDeRevenuJointSelfViewBean();
    }

}