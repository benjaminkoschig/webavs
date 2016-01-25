/**
 * 
 */
package globaz.al.vb.dossier;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle permettant la recherche sur les EnvoiComplexModel
 * 
 * @author dhi
 * 
 */
public class ALDossierEnvoiListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Modèle de recherche pour les envois
     */
    private EnvoiComplexModelSearch searchModel = null;

    /**
	 * 
	 */
    public ALDossierEnvoiListViewBean() {
        super();
        searchModel = new EnvoiComplexModelSearch();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = ALServiceLocator.getEnvoiComplexService().search(searchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new ALDossierEnvoiViewBean(
                (EnvoiComplexModel) searchModel.getSearchResults()[idx]) : new ALDossierEnvoiViewBean();
    }

    /**
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
    public EnvoiComplexModelSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(EnvoiComplexModelSearch searchModel) {
        this.searchModel = searchModel;
    }

}
