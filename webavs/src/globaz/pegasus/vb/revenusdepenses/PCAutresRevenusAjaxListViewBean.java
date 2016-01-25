package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutresRevenusAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AutresRevenusSearch searchModel = null;

    /**
	 * 
	 */
    public PCAutresRevenusAjaxListViewBean() {
        super();
        searchModel = new AutresRevenusSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAutresRevenus(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAutresRevenusAjaxViewBean(
                (AutresRevenus) searchModel.getSearchResults()[idx]) : new PCAutresRevenusAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AutresRevenusSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AutresRevenusSearch searchModel) {
        this.searchModel = searchModel;
    }

}