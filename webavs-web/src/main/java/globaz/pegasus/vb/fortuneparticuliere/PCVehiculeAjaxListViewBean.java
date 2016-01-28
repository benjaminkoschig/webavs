package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.VehiculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCVehiculeAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private VehiculeSearch searchModel = null;

    /**
	 * 
	 */
    public PCVehiculeAjaxListViewBean() {
        super();
        searchModel = new VehiculeSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchVehicule(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCVehiculeAjaxViewBean((Vehicule) searchModel.getSearchResults()[idx])
                : new PCVehiculeAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public VehiculeSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(VehiculeSearch searchModel) {
        this.searchModel = searchModel;
    }

}
