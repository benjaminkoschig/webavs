package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuHypothetiqueAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private RevenuHypothetiqueSearch searchModel = null;

    /**
	 * 
	 */
    public PCRevenuHypothetiqueAjaxListViewBean() {
        super();
        searchModel = new RevenuHypothetiqueSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchRevenuHypothetique(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCRevenuHypothetiqueAjaxViewBean(
                (RevenuHypothetique) searchModel.getSearchResults()[idx]) : new PCRevenuHypothetiqueAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public RevenuHypothetiqueSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(RevenuHypothetiqueSearch searchModel) {
        this.searchModel = searchModel;
    }

}