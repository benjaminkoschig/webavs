package globaz.pegasus.vb.revenusdepenses;

import ch.globaz.pegasus.business.models.revenusdepenses.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class PCFraisGardeAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private FraisGardeSearch searchModel = null;

    /**
     *
     */
    public PCFraisGardeAjaxListViewBean() {
        super();
        searchModel = new FraisGardeSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchFraisGarde(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCFraisGardeAjaxViewBean(
                (FraisGarde) searchModel.getSearchResults()[idx]) : new PCFraisGardeAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public FraisGardeSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(FraisGardeSearch searchModel) {
        this.searchModel = searchModel;
    }

}