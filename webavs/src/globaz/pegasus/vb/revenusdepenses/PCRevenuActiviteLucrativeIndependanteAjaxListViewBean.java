package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuActiviteLucrativeIndependanteAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private RevenuActiviteLucrativeIndependanteSearch searchModel = null;

    /**
	 * 
	 */
    public PCRevenuActiviteLucrativeIndependanteAjaxListViewBean() {
        super();
        searchModel = new RevenuActiviteLucrativeIndependanteSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchRevenuActiviteLucrativeIndependante(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCRevenuActiviteLucrativeIndependanteAjaxViewBean(
                (RevenuActiviteLucrativeIndependante) searchModel.getSearchResults()[idx])
                : new PCRevenuActiviteLucrativeIndependanteAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public RevenuActiviteLucrativeIndependanteSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(RevenuActiviteLucrativeIndependanteSearch searchModel) {
        this.searchModel = searchModel;
    }

}