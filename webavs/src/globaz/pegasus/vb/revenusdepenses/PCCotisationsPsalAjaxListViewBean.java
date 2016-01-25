package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCCotisationsPsalAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CotisationsPsalSearch searchModel = null;

    /**
	 * 
	 */
    public PCCotisationsPsalAjaxListViewBean() {
        super();
        searchModel = new CotisationsPsalSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchCotisationsPsal(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCCotisationsPsalAjaxViewBean(
                (CotisationsPsal) searchModel.getSearchResults()[idx]) : new PCCotisationsPsalAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public CotisationsPsalSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(CotisationsPsalSearch searchModel) {
        this.searchModel = searchModel;
    }

}