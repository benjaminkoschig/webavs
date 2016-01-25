package globaz.pegasus.vb.habitat;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.renteijapi.PCAllocationImpotentAjaxViewBean;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTaxeJournaliereHomeAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private TaxeJournaliereHomeSearch searchModel = null;

    public PCTaxeJournaliereHomeAjaxListViewBean() {
        super();
        searchModel = new TaxeJournaliereHomeSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchTaxeJournaliereHome(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAllocationImpotentAjaxViewBean(
                (AllocationImpotent) searchModel.getSearchResults()[idx]) : new PCAllocationImpotentAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public TaxeJournaliereHomeSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(TaxeJournaliereHomeSearch searchModel) {
        this.searchModel = searchModel;
    }
}
