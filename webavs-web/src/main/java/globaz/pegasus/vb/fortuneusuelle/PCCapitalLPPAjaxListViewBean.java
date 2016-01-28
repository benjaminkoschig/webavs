package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCCapitalLPPAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CapitalLPPSearch searchModel = null;

    /**
	 * 
	 */
    public PCCapitalLPPAjaxListViewBean() {
        super();
        searchModel = new CapitalLPPSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchCapitalLPP(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCCapitalLPPAjaxViewBean(
                (CapitalLPP) searchModel.getSearchResults()[idx]) : new PCCapitalLPPAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public CapitalLPPSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(CapitalLPPSearch searchModel) {
        this.searchModel = searchModel;
    }

}
