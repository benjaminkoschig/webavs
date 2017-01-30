package globaz.amal.vb.sedexco;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCOSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSedexcoListViewBean extends BJadePersistentObjectListViewBean {
    ComplexAnnonceSedexCOSearch searchModel = null;

    public AMSedexcoListViewBean() {
        super();
        searchModel = new ComplexAnnonceSedexCOSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = AmalServiceLocator.getComplexAnnonceSedexCOService().search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new AMSedexcoViewBean(
                (ComplexAnnonceSedexCO) searchModel.getSearchResults()[idx]) : new AMSedexcoViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public ComplexAnnonceSedexCOSearch getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(ComplexAnnonceSedexCOSearch searchModel) {
        this.searchModel = searchModel;
    }
}
