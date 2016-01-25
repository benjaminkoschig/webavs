package globaz.musca.db.facturation;

public class FAModuleFacturationListViewBean extends FAModuleFacturationManager implements
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
        return ((FAModuleFacturation) getEntity(pos)).getIdModuleFacturation();
    }

    public String getLibelleModule(int pos) {
        return ((FAModuleFacturation) getEntity(pos)).getLibelle();
    }

    public String getLibelleType(int pos) {
        return ((FAModuleFacturation) getEntity(pos)).getLibelleType();
    }

    public Boolean getModifierAfact(int pos) {
        return ((FAModuleFacturation) getEntity(pos)).isModifierAfact();
    }

    public String getNiveauAppel(int pos) {
        return ((FAModuleFacturation) getEntity(pos)).getNiveauAppel();
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
