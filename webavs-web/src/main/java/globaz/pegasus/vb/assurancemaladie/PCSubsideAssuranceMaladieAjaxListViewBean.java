package globaz.pegasus.vb.assurancemaladie;

import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class PCSubsideAssuranceMaladieAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private SubsideAssuranceMaladieSearch searchModel = null;

    /**
     *
     */
    public PCSubsideAssuranceMaladieAjaxListViewBean() {
        super();
        searchModel = new SubsideAssuranceMaladieSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getAssuranceMaladieService().searchSubsideAssuranceMaladie(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCSubsideAssuranceMaladieAjaxViewBean(
                (SubsideAssuranceMaladie) searchModel.getSearchResults()[idx]) : new PCSubsideAssuranceMaladieAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public SubsideAssuranceMaladieSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(SubsideAssuranceMaladieSearch searchModel) {
        this.searchModel = searchModel;
    }

}
