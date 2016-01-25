package globaz.naos.db.planAssurance;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité PlanAssurance.
 * 
 * @author sau
 */
public class AFPlanAssuranceListViewBean extends AFPlanAssuranceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getAssuranceId(int index) {

        return ((AFPlanAssurance) getEntity(index)).getAssuranceId();
    }

    public String getPlanId(int index) {

        return ((AFPlanAssurance) getEntity(index)).getPlanId();
    }
}
