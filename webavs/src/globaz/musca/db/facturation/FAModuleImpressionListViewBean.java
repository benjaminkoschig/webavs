package globaz.musca.db.facturation;

public class FAModuleImpressionListViewBean extends FAModuleImpressionManager implements
        globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    public java.lang.String getAction() {
        return action;
    }

    public String getIdModuleImpression(int pos) {
        return ((FAModuleImpression) getEntity(pos)).getIdModuleImpression();
    }

    public String getLibelleMode(int pos) {
        try {
            return globaz.musca.translation.CodeSystem.getLibelle(getSession(),
                    ((FAModuleImpression) getEntity(pos)).getIdModeRecouvrement());
        } catch (Exception e) {
            return "";
        }
    }

    public String getLibelleModule(int pos) {
        return ((FAModuleImpression) getEntity(pos)).getLibelle();
    }

    public String getLibelleType(int pos) {
        try {
            return globaz.musca.translation.CodeSystem.getLibelle(getSession(),
                    ((FAModuleImpression) getEntity(pos)).getIdCritereDecompte());
        } catch (Exception e) {
            {
                return "";
            }
        }
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
