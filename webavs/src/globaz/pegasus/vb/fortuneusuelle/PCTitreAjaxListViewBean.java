package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTitreAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private TitreSearch searchModel = null;

    /**
	 * 
	 */
    public PCTitreAjaxListViewBean() {
        super();
        searchModel = new TitreSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchTitre(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCTitreAjaxViewBean((Titre) searchModel.getSearchResults()[idx])
                : new PCTitreAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public TitreSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(TitreSearch searchModel) {
        this.searchModel = searchModel;
    }

}
