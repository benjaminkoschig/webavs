/*
 * Créé le 20 août 07
 */

package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REAnnexeDecision;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author BSC
 * 
 */

public class REAnnexeDecisionViewBean extends REAnnexeDecision implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String idProvisoire = "";

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    public String getIdProvisoire() {
        return idProvisoire;
    }

    public void setIdProvisoire(String idProvisoire) {
        this.idProvisoire = idProvisoire;
    }

}