package globaz.pegasus.vb.fortuneusuelle;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutresDettesProuveesAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private AutresDettesProuveesSearch searchModel = null;

    /**
	 * 
	 */
    public PCAutresDettesProuveesAjaxListViewBean() {
        super();
        searchModel = new AutresDettesProuveesSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchAutresDettesProuvees(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCAutresDettesProuveesAjaxViewBean(
                (AutresDettesProuvees) searchModel.getSearchResults()[idx]) : new PCAutresDettesProuveesAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public AutresDettesProuveesSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(AutresDettesProuveesSearch searchModel) {
        this.searchModel = searchModel;
    }

}
