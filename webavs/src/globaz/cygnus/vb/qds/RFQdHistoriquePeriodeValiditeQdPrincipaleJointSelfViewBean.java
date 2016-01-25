/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author jje
 */
public class RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfViewBean extends
        RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf {

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
            gestionnaireId = super.getIdGestionnaire();
        } else {
            gestionnaireId = super.getAncienGestionnaire();
        }

        return gestionnaireId;
    }

}
