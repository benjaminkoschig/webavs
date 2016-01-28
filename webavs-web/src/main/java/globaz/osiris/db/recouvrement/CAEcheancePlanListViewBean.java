package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;

/**
 * Représente le model de la vue "_rcListe".
 * 
 * @author Pascal Lovy, 12-may-2005
 */
public class CAEcheancePlanListViewBean extends CAEcheancePlanManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcheancePlanViewBean();
    }

    /**
     * @return true si la caisse gère les rappels sur les plans
     */
    public boolean hasRappelSurPlan() {
        return CAApplication.getApplicationOsiris().getCAParametres().isRappelSurPlan();
    }

}
