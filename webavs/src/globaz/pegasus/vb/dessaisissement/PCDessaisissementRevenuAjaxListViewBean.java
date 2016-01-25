package globaz.pegasus.vb.dessaisissement;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDessaisissementRevenuAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private DessaisissementRevenuSearch searchModel = null;

    /**
	 * 
	 */
    public PCDessaisissementRevenuAjaxListViewBean() {
        super();
        searchModel = new DessaisissementRevenuSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchDessaisissementRevenu(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCDessaisissementRevenuAjaxViewBean(
                (DessaisissementRevenu) searchModel.getSearchResults()[idx])
                : new PCDessaisissementRevenuAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public DessaisissementRevenuSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(DessaisissementRevenuSearch searchModel) {
        this.searchModel = searchModel;
    }

}
