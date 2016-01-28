// créé le 24 mars 2010
package globaz.cygnus.vb.conventions;

import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointConventionAssureManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFRechercheConventionListViewBean extends
        RFConventionJointAssConFouTsJointFournisseurJointConventionAssureManager implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFRechercheConventionViewBean();
    }

}
