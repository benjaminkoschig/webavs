package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPretEnversTiersAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private PretEnversTiersSearch searchModel = null;

    /**
	 * 
	 */
    public PCPretEnversTiersAjaxListViewBean() {
        super();
        searchModel = new PretEnversTiersSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchPretEnversTiers(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCPretEnversTiersAjaxViewBean(
                (PretEnversTiers) searchModel.getSearchResults()[idx]) : new PCPretEnversTiersAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public PretEnversTiersSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(PretEnversTiersSearch searchModel) {
        this.searchModel = searchModel;
    }

}
