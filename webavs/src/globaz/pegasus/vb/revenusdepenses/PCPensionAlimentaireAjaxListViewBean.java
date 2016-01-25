package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPensionAlimentaireAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private PensionAlimentaireSearch searchModel = null;

    /**
	 * 
	 */
    public PCPensionAlimentaireAjaxListViewBean() {
        super();
        searchModel = new PensionAlimentaireSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchPensionAlimentaire(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCPensionAlimentaireAjaxViewBean(
                (PensionAlimentaire) searchModel.getSearchResults()[idx]) : new PCPensionAlimentaireAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public PensionAlimentaireSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(PensionAlimentaireSearch searchModel) {
        this.searchModel = searchModel;
    }

}