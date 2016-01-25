package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCNumeraireAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private NumeraireSearch searchModel = null;

    /**
	 * 
	 */
    public PCNumeraireAjaxListViewBean() {
        super();
        searchModel = new NumeraireSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchNumeraire(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCNumeraireAjaxViewBean(
                (Numeraire) searchModel.getSearchResults()[idx]) : new PCNumeraireAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public NumeraireSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(NumeraireSearch searchModel) {
        this.searchModel = searchModel;
    }

}
