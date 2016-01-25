/*
 * Créé le 24 mars 2010
 */
package globaz.cygnus.vb.conventions;

import globaz.cygnus.db.conventions.RFMontantsConventionManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * author fha
 */
public class RFRechercheMontantsConventionListViewBean extends RFMontantsConventionManager implements
        FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFRechercheMontantsConventionViewBean();
    }

}