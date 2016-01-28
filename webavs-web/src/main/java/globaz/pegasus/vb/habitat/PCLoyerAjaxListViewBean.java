package globaz.pegasus.vb.habitat;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.renteijapi.PCAllocationImpotentAjaxViewBean;
import ch.globaz.pegasus.business.models.habitat.LoyerSearch;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCLoyerAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private LoyerSearch searchModel = null;

    public PCLoyerAjaxListViewBean() {
        super();
        searchModel = new LoyerSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchLoyer(searchModel);
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
    public LoyerSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(LoyerSearch searchModel) {
        this.searchModel = searchModel;
    }

}
