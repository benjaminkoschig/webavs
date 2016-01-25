package globaz.naos.db.plan;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité Plan.
 * 
 * @author administrator
 */
public class AFPlanListViewBean extends AFPlanManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getPlanId(int index) {

        return ((AFPlan) getEntity(index)).getPlanId();
    }

    public String getPlanLibelle(int index) {

        return ((AFPlan) getEntity(index)).getPlanLibelle();
    }
}
