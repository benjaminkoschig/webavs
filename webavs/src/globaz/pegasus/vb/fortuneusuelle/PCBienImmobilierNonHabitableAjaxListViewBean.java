package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCBienImmobilierNonHabitableAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private BienImmobilierNonHabitableSearch searchModel = null;

    /**
	 * 
	 */
    public PCBienImmobilierNonHabitableAjaxListViewBean() {
        super();
        searchModel = new BienImmobilierNonHabitableSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchBienImmobilierNonHabitable(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCBienImmobilierNonHabitableAjaxViewBean(
                (BienImmobilierNonHabitable) searchModel.getSearchResults()[idx])
                : new PCBienImmobilierNonHabitableAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public BienImmobilierNonHabitableSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(BienImmobilierNonHabitableSearch searchModel) {
        this.searchModel = searchModel;
    }

}
