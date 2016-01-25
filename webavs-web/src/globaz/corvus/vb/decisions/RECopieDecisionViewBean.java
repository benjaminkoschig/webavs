/*
 * Créé le 20 août 07
 */

package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.RECopieDecision;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author BSC
 * 
 */

public class RECopieDecisionViewBean extends RECopieDecision implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idProvisoire = "";

    private PRTiersWrapper tiersCopie = null;

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    public String getIdProvisoire() {
        return idProvisoire;
    }

    /**
     * 
     * @return
     */
    public String getTiersDescription() {

        if (JadeStringUtil.isBlankOrZero(getIdTiersCopie())) {
            return "TIERS INCONNU";
        }

        if (loadTiers()) {
            return tiersCopie.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersCopie.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        }

        return "";
    }

    /**
     * 
     * @return
     */
    private boolean loadTiers() {
        if (tiersCopie == null) {

            try {
                tiersCopie = PRTiersHelper.getTiersParId(getSession(), getIdTiersCopie());

                if (tiersCopie == null) {
                    tiersCopie = PRTiersHelper.getAdministrationParId(getSession(), getIdTiersCopie());
                }
            } catch (Exception e) {
                getSession().addError("Le Tiers " + getIdTiersCopie() + "ne peut pas être chargée.");
            }
        }
        return tiersCopie != null;
    }

    public void setIdProvisoire(String idProvisoire) {
        this.idProvisoire = idProvisoire;
    }

}