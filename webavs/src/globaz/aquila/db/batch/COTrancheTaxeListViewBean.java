package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COTrancheTaxeManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class COTrancheTaxeListViewBean extends COTrancheTaxeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCalculTaxe = new String();

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COTrancheTaxeViewBean();
    }

    @Override
    public String getForIdCalculTaxe() {
        return forIdCalculTaxe;
    }

    @Override
    public void setForIdCalculTaxe(String forIdCalculTaxe) {
        this.forIdCalculTaxe = forIdCalculTaxe;
    }
}
