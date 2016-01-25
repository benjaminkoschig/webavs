/*
 * Créé le 26 juil. 07
 */

package globaz.corvus.vb.decisions;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author SCR
 * 
 */

public class REDecisionsICViewBean extends REDecisionsViewBean implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private REDecisionsContainer decisionsContainer = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idTiersRequerant = null;

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    @Override
    public String getCsEtatDecisionLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    @Override
    public String getCsTypeDecisionLibelle() {
        return getSession().getCodeLibelle(getCsTypeDecision());
    }

    public REDecisionsContainer getDecisionsContainer() {
        return decisionsContainer;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public void setDecisionsContainer(REDecisionsContainer decisionsContainer) {
        this.decisionsContainer = decisionsContainer;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

}