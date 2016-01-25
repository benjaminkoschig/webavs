package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.IjApgSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCIjApgAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private IjApgSearch searchModel = null;

    public PCIjApgAjaxListViewBean() {
        super();
        searchModel = new IjApgSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchIjApg(searchModel);

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
    public IjApgSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(IjApgSearch searchModel) {
        this.searchModel = searchModel;
    }

}
