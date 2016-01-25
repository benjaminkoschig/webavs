package globaz.helios.db.comptes;

import globaz.helios.translation.CodeSystem;

public class CGPlanComptableListViewBean extends CGPlanComptableManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CGPlanComptableListViewBean() {
        wantCallMethodBeforeFind(true);
    }

    public String getSoldeExercice(int i) {

        CGPlanComptableViewBean entity = (CGPlanComptableViewBean) getEntity(i);
        if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
            return entity.getSolde();
        }
        return entity.getSoldeProvisoire();

    }
}
