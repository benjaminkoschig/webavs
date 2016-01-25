package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotentSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * ListviewBean pour les allocation impotentsAi 6.2010
 * 
 * @author SCE
 * 
 */
public class PCAllocationImpotentAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AllocationImpotentSearch searchModel = null;

    /**
     * Constructeur
     */
    public PCAllocationImpotentAjaxListViewBean() {
        super();
        searchModel = new AllocationImpotentSearch();
    }

    /**
     * Recherche d'après critère du modèle
     */
    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAllocationImpotent(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAllocationImpotentAjaxViewBean(
                (AllocationImpotent) searchModel.getSearchResults()[idx]) : new PCAllocationImpotentAjaxViewBean();
    }

    /**
     * Retourne le manager
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AllocationImpotentSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AllocationImpotentSearch searchModel) {
        this.searchModel = searchModel;
    }
}
