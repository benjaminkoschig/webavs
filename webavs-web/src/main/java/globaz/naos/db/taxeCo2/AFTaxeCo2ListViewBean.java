package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;

public class AFTaxeCo2ListViewBean extends AFTaxeCo2Manager implements globaz.framework.bean.FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFTaxeCo2ViewBean();
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getEtatTaxe(int pos) {
        return getSession().getCodeLibelle(((AFTaxeCo2) getEntity(pos)).getEtat());
    }

    public String getLibelleMotifFin(int pos) {
        return getSession().getCodeLibelle(((AFTaxeCo2) getEntity(pos)).getMotifFin());
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

}
