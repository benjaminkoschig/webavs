package globaz.amal.vb.sedexpt;

import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import globaz.amal.vb.sedexrp.AMSedexrpViewBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class AMSedexptListViewBean extends BJadePersistentObjectListViewBean {
    ComplexAnnonceSedexSearch searchModel = null;

    public AMSedexptListViewBean() {
        super();
        searchModel = new ComplexAnnonceSedexSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = AmalServiceLocator.getComplexAnnonceSedexService().search(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new AMSedexptViewBean(
                (ComplexAnnonceSedex) searchModel.getSearchResults()[idx]) : new AMSedexptViewBean();
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
