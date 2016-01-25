package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAssuranceRenteViagereAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AssuranceRenteViagereSearch searchModel = null;

    /**
	 * 
	 */
    public PCAssuranceRenteViagereAjaxListViewBean() {
        super();
        searchModel = new AssuranceRenteViagereSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAssuranceRenteViagere(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAssuranceRenteViagereAjaxViewBean(
                (AssuranceRenteViagere) searchModel.getSearchResults()[idx])
                : new PCAssuranceRenteViagereAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AssuranceRenteViagereSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AssuranceRenteViagereSearch searchModel) {
        this.searchModel = searchModel;
    }

}
