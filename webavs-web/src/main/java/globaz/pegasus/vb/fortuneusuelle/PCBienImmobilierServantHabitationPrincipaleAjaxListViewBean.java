package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCBienImmobilierServantHabitationPrincipaleAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private BienImmobilierServantHabitationPrincipaleSearch searchModel = null;

    /**
	 * 
	 */
    public PCBienImmobilierServantHabitationPrincipaleAjaxListViewBean() {
        super();
        searchModel = new BienImmobilierServantHabitationPrincipaleSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchBienImmobilierServantHabitationPrincipale(
                searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCBienImmobilierServantHabitationPrincipaleAjaxViewBean(
                (BienImmobilierServantHabitationPrincipale) searchModel.getSearchResults()[idx])
                : new PCBienImmobilierServantHabitationPrincipaleAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public BienImmobilierServantHabitationPrincipaleSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(BienImmobilierServantHabitationPrincipaleSearch searchModel) {
        this.searchModel = searchModel;
    }

}
