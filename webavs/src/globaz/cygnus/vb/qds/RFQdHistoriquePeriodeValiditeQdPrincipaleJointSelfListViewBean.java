/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfListViewBean extends
        RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfListViewBean loadHistorique(BSession session,
            String idQd) throws Exception {

        RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfListViewBean retValue = null;

        if (!JadeStringUtil.isNull(idQd)) {
            retValue = new RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfListViewBean();
            retValue.setForIdQd(idQd);
            retValue.setSession(session);
            retValue.changeManagerSize(0);
            retValue.find();
        }

        return retValue;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfViewBean();

    }

}