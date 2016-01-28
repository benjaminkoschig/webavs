/**
 * 
 */
package globaz.perseus.vb.demande;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFDemandeListViewBean extends BJadePersistentObjectListViewBean {

    private DemandeSearchModel searchModel;

    /**
	 * 
	 */
    public PFDemandeListViewBean() throws Exception {
        super();
        searchModel = new DemandeSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getDemandeService().search(searchModel);
        getISession().setAttribute("likeNss", searchModel.getLikeNss());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFDemandeViewBean((Demande) searchModel.getSearchResults()[idx])
                : new PFDemandeViewBean();
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
    public DemandeSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(DemandeSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
