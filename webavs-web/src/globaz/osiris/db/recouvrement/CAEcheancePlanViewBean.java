package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;

/**
 * Représente le model de la vue "_de".
 * 
 * @author Pascal Lovy, 12-may-2005
 */
public class CAEcheancePlanViewBean extends CAEcheancePlan implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return true si la caisse gère les rappels sur les plans
     */
    public boolean hasRappelSurPlan() {
        return CAApplication.getApplicationOsiris().getCAParametres().isRappelSurPlan();
    }
}
