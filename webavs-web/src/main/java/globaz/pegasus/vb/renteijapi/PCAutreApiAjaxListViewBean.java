package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutreApiAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AutreApiSearch searchModel = null;

    public PCAutreApiAjaxListViewBean() {
        super();
        searchModel = new AutreApiSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAutreApi(searchModel);

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAutreRenteAjaxViewBean(
                (AutreRente) searchModel.getSearchResults()[idx]) : new PCAutreRenteAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AutreApiSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AutreApiSearch searchModel) {
        this.searchModel = searchModel;
    }

}
