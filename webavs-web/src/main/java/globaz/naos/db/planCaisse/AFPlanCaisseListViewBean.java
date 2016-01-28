/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planCaisse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

/**
 * Le listViewBean de l'entité PlanCaisse.
 * 
 * @author sau
 */
public class AFPlanCaisseListViewBean extends AFPlanCaisseManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TIAdministrationViewBean getAdministration(int index) {

        return ((AFPlanCaisse) getEntity(index)).getAdministration();
    }

    public String getIdTiers(int index) {

        return ((AFPlanCaisse) getEntity(index)).getIdTiers();
    }

    public String getLibelle(int index) {

        return ((AFPlanCaisse) getEntity(index)).getLibelle();
    }

    public String getPlanCaisseId(int index) {

        return ((AFPlanCaisse) getEntity(index)).getPlanCaisseId();
    }
}
