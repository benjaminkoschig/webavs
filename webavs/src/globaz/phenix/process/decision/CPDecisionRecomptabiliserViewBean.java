/**
 * class CPListeDecisionsAvecMiseEnCompteViewBean �crit le 19/01/05 par JPA
 * 
 * class ViewBean pour les d�cisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.process.decision;

import globaz.framework.bean.FWViewBeanInterface;

public class CPDecisionRecomptabiliserViewBean extends CPProcessReporterDecisionPreEncodee implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String idDecision = "";

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }
}
