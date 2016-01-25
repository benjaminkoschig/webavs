package globaz.pegasus.vb.renteijapi;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * ListViewBean pour les rentes AvsAi 6.2010
 * 
 * @author SCE
 * 
 */
public class PCRenteAvsAiAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private RenteAvsAiSearch searchModel = null;

    /**
     * Constructeur
     */
    public PCRenteAvsAiAjaxListViewBean() {
        super();
        searchModel = new RenteAvsAiSearch();
    }

    /**
     * Recherche d'après critère du modèle
     */
    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchRenteAvsAi(searchModel);

    }

    /**
     * Retourne l'identifiant de l'entité
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCRenteAvsAiAjaxViewBean(
                (RenteAvsAi) searchModel.getSearchResults()[idx]) : new PCRenteAvsAiAjaxViewBean();
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
    public RenteAvsAiSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(RenteAvsAiSearch searchModel) {
        this.searchModel = searchModel;
    }
}
