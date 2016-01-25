package globaz.cygnus.vb.attestations;

import globaz.cygnus.db.attestations.RFAttestationJointDossierJointTiersManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFRechercheAttestationListViewBean extends RFAttestationJointDossierJointTiersManager implements
        FWViewBeanInterface {
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
        return new RFRechercheAttestationViewBean();
    }

}
