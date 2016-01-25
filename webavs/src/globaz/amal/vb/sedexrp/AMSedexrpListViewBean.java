package globaz.amal.vb.sedexrp;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSedexrpListViewBean extends BJadePersistentObjectListViewBean {
    ComplexAnnonceSedexSearch searchModel = null;

    public AMSedexrpListViewBean() {
        super();
        searchModel = new ComplexAnnonceSedexSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = AmalServiceLocator.getComplexAnnonceSedexService().search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new AMSedexrpViewBean(
                (ComplexAnnonceSedex) searchModel.getSearchResults()[idx]) : new AMSedexrpViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public ComplexAnnonceSedexSearch getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(ComplexAnnonceSedexSearch searchModel) {
        this.searchModel = searchModel;
    }
}
