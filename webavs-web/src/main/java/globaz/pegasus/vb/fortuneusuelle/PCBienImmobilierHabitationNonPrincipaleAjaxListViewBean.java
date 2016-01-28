package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCBienImmobilierHabitationNonPrincipaleAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private BienImmobilierHabitationNonPrincipaleSearch searchModel = null;

    /**
	 * 
	 */
    public PCBienImmobilierHabitationNonPrincipaleAjaxListViewBean() {
        super();
        searchModel = new BienImmobilierHabitationNonPrincipaleSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchBienImmobilierHabitationNonPrincipale(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCBienImmobilierHabitationNonPrincipaleAjaxViewBean(
                (BienImmobilierHabitationNonPrincipale) searchModel.getSearchResults()[idx])
                : new PCBienImmobilierHabitationNonPrincipaleAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public BienImmobilierHabitationNonPrincipaleSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(BienImmobilierHabitationNonPrincipaleSearch searchModel) {
        this.searchModel = searchModel;
    }

}
