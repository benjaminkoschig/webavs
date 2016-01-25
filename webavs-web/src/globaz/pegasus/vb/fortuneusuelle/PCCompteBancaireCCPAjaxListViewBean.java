package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCPSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCCompteBancaireCCPAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CompteBancaireCCPSearch searchModel = null;

    /**
	 * 
	 */
    public PCCompteBancaireCCPAjaxListViewBean() {
        super();
        searchModel = new CompteBancaireCCPSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchCompteBancaireCCP(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCCompteBancaireCCPAjaxViewBean(
                (CompteBancaireCCP) searchModel.getSearchResults()[idx]) : new PCCompteBancaireCCPAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public CompteBancaireCCPSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(CompteBancaireCCPSearch searchModel) {
        this.searchModel = searchModel;
    }

}
