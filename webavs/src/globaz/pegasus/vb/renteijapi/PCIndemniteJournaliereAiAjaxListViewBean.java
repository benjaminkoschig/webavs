package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * ListviewBean pour les IndemnitesJournalieresAi 6.2010
 * 
 * @author SCE
 * 
 */
public class PCIndemniteJournaliereAiAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private IndemniteJournaliereAiSearch searchModel = null;

    /**
     * Constructeur
     */
    public PCIndemniteJournaliereAiAjaxListViewBean() {
        super();
        searchModel = new IndemniteJournaliereAiSearch();
    }

    /**
     * Recherche d'après critère du modèle
     */
    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchIndemniteJournaliereAi(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCIndemniteJournaliereAiAjaxViewBean(
                (IndemniteJournaliereAi) searchModel.getSearchResults()[idx])
                : new PCIndemniteJournaliereAiAjaxViewBean();
    }

    /**
     * Retourne le manager
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public IndemniteJournaliereAiSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(IndemniteJournaliereAiSearch searchModel) {
        this.searchModel = searchModel;
    }
}
