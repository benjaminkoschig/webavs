/**
 * 
 */
package globaz.al.vb.parametres;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * 
 * ViewBean gérant la recherche sur les éléments SignetListModel
 * 
 * @author dhi
 * 
 */
public class ALSignetsListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Modèle de recherche pour les signets
     */
    private SignetListModelSearch searchModel = null;

    /**
     * Default constructor
     */
    public ALSignetsListViewBean() {
        searchModel = new SignetListModelSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel.setWhereKey("libelle");
        searchModel.setOrderKey("libelleOrder");
        searchModel = ENServiceLocator.getSignetListModelService().search(searchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new ALSignetsViewBean(
                (SignetListModel) searchModel.getSearchResults()[idx]) : new ALSignetsViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public SignetListModelSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(SignetListModelSearch searchModel) {
        this.searchModel = searchModel;
    }

}
