package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAllocationsFamilialesAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AllocationsFamilialesSearch searchModel = null;

    /**
	 * 
	 */
    public PCAllocationsFamilialesAjaxListViewBean() {
        super();
        searchModel = new AllocationsFamilialesSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAllocationsFamiliales(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAllocationsFamilialesAjaxViewBean(
                (AllocationsFamiliales) searchModel.getSearchResults()[idx])
                : new PCAllocationsFamilialesAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AllocationsFamilialesSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AllocationsFamilialesSearch searchModel) {
        this.searchModel = searchModel;
    }

}