package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCMarchandisesStockAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private MarchandisesStockSearch searchModel = null;

    /**
	 * 
	 */
    public PCMarchandisesStockAjaxListViewBean() {
        super();
        searchModel = new MarchandisesStockSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchMarchandisesStock(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCMarchandisesStockAjaxViewBean(
                (MarchandisesStock) searchModel.getSearchResults()[idx]) : new PCMarchandisesStockAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public MarchandisesStockSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(MarchandisesStockSearch searchModel) {
        this.searchModel = searchModel;
    }

}
