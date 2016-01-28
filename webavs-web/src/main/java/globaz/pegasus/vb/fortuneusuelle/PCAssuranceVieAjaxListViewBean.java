package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAssuranceVieAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AssuranceVieSearch searchModel = null;

    /**
	 * 
	 */
    public PCAssuranceVieAjaxListViewBean() {
        super();
        searchModel = new AssuranceVieSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAssuranceVie(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAssuranceVieAjaxViewBean(
                (AssuranceVie) searchModel.getSearchResults()[idx]) : new PCAssuranceVieAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AssuranceVieSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AssuranceVieSearch searchModel) {
        this.searchModel = searchModel;
    }

}