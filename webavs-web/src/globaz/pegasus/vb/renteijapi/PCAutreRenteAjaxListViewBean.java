package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutreRenteAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AutreRenteSearch searchModel = null;

    public PCAutreRenteAjaxListViewBean() {
        super();
        searchModel = new AutreRenteSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAutreRente(searchModel);

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
    public AutreRenteSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AutreRenteSearch searchModel) {
        this.searchModel = searchModel;
    }

}
