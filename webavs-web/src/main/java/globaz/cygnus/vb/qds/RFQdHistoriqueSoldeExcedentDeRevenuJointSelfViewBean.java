/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFQdHistoriqueSoldeExcedentDeRevenuJointSelf;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author jje
 */
public class RFQdHistoriqueSoldeExcedentDeRevenuJointSelfViewBean extends RFQdHistoriqueSoldeExcedentDeRevenuJointSelf {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // champs nécessaires description tiers

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.cygnus.db.qds.RFQdTestHistoriqueJointSelf#getAncienGestionnaire()
     */
    @Override
    public String getAncienGestionnaire() throws Exception {
        // s'il n'y a pas d'ancien gestionnaire, prendre l'actuel.
        String gestionnaireId;
        if (JadeStringUtil.isBlank(super.getAncienGestionnaire())) {
            gestionnaireId = super.getVisaGestionnaire();
        } else {
            gestionnaireId = super.getAncienGestionnaire();
        }

        return gestionnaireId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.cygnus.db.qds.RFQdTestHistorique#getVisaGestionnaire()
     *//*
        * public String getVisaGestionnaire() throws Exception { return PRGestionnaireHelper
        * .getGestionnaire(super.getVisaGestionnaire()).getVisa(); }
        */

}
