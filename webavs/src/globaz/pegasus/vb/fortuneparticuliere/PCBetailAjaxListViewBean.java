package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCBetailAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private BetailSearch searchModel = null;

    /**
	 * 
	 */
    public PCBetailAjaxListViewBean() {
        super();
        searchModel = new BetailSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchBetail(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCBetailAjaxViewBean((Betail) searchModel.getSearchResults()[idx])
                : new PCBetailAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public BetailSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(BetailSearch searchModel) {
        this.searchModel = searchModel;
    }

}
