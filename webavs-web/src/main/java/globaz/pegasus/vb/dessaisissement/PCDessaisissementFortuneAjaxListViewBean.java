package globaz.pegasus.vb.dessaisissement;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDessaisissementFortuneAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private DessaisissementFortuneSearch searchModel = null;

    /**
	 * 
	 */
    public PCDessaisissementFortuneAjaxListViewBean() {
        super();
        searchModel = new DessaisissementFortuneSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchDessaisissementFortune(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCDessaisissementFortuneAjaxViewBean(
                (DessaisissementFortune) searchModel.getSearchResults()[idx])
                : new PCDessaisissementFortuneAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public DessaisissementFortuneSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(DessaisissementFortuneSearch searchModel) {
        this.searchModel = searchModel;
    }

}
