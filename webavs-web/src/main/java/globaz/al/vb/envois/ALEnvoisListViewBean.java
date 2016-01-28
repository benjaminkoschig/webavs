/**
 * 
 */
package globaz.al.vb.envois;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant la recherche sur les éléments EnvoiComplexModel
 * 
 * @author dhi
 * 
 */
public class ALEnvoisListViewBean extends BJadePersistentObjectListViewBean {

    private EnvoiComplexModelSearch searchModel = null;

    /**
	 * 
	 */
    public ALEnvoisListViewBean() {
        super();
        searchModel = new EnvoiComplexModelSearch();
    }

    /*
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
        return idx < searchModel.getSize() ? new ALEnvoisViewBean(
                (EnvoiComplexModel) searchModel.getSearchResults()[idx]) : new ALEnvoisViewBean();
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
