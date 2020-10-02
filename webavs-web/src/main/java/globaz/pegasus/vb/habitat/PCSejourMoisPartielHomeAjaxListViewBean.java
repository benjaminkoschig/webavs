package globaz.pegasus.vb.habitat;

import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.renteijapi.PCAllocationImpotentAjaxViewBean;

public class PCSejourMoisPartielHomeAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SejourMoisPartielHomeSearch searchModel = null;

    public PCSejourMoisPartielHomeAjaxListViewBean() {
        super();
        searchModel = new SejourMoisPartielHomeSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchSejourMoisPartielHome(searchModel);
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
    public SejourMoisPartielHomeSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(SejourMoisPartielHomeSearch searchModel) {
        this.searchModel = searchModel;
    }
}
