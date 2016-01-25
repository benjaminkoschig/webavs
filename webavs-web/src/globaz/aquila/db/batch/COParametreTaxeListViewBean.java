package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COParametreTaxeManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class COParametreTaxeListViewBean extends COParametreTaxeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdCalculTaxe = new String();
    private java.lang.String forIdRubrique = new String();

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COParametreTaxeViewBean();
    }

    @Override
    public java.lang.String getForIdCalculTaxe() {
        return forIdCalculTaxe;
    }

    @Override
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    @Override
    public void setForIdCalculTaxe(java.lang.String forIdCalculTaxe) {
        this.forIdCalculTaxe = forIdCalculTaxe;
    }

    @Override
    public void setForIdRubrique(java.lang.String forIdRubrique) {
        this.forIdRubrique = forIdRubrique;
    }
}
