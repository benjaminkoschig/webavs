package globaz.pegasus.vb.assurancemaladie;

import ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGardeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.revenusdepenses.PCFraisGardeAjaxViewBean;

public class PCPrimeAssuranceMaladieAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private PrimeAssuranceMaladieSearch searchModel = null;

    /**
     *
     */
    public PCPrimeAssuranceMaladieAjaxListViewBean() {
        super();
        searchModel = new PrimeAssuranceMaladieSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getAssuranceMaladieService().searchPrimeAssuranceMaladie(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCPrimeAssuranceMaladieAjaxViewBean(
                (PrimeAssuranceMaladie) searchModel.getSearchResults()[idx]) : new PCPrimeAssuranceMaladieAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public PrimeAssuranceMaladieSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(PrimeAssuranceMaladieSearch searchModel) {
        this.searchModel = searchModel;
    }

}
