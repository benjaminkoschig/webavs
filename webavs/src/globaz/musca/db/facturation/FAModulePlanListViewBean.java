package globaz.musca.db.facturation;

public class FAModulePlanListViewBean extends FAModulePlanManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    public java.lang.String getAction() {
        return action;
    }

    public String getIdModuleFacturation(int pos) {
        return ((FAModulePlan) getEntity(pos)).getIdModuleFacturation();
    }

    public String getIdPlanFacturation(int pos) {
        return ((FAModulePlan) getEntity(pos)).getIdPlanFacturation();
    }

    /*
     * Recherche du libellé du module de facturation
     */
    public String getLibelleModule(int pos) {
        return ((FAModulePlan) getEntity(pos)).getLibelle();

    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
