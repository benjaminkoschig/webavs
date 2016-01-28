package globaz.pegasus.vb.fortuneparticuliere;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutreFortuneMobiliereAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AutreFortuneMobiliereSearch searchModel = null;

    /**
	 * 
	 */
    public PCAutreFortuneMobiliereAjaxListViewBean() {
        super();
        searchModel = new AutreFortuneMobiliereSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAutreFortuneMobiliere(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAutreFortuneMobiliereAjaxViewBean(
                (AutreFortuneMobiliere) searchModel.getSearchResults()[idx])
                : new PCAutreFortuneMobiliereAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AutreFortuneMobiliereSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AutreFortuneMobiliereSearch searchModel) {
        this.searchModel = searchModel;
    }

}
