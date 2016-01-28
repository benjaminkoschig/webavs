/**
 * 
 */
package globaz.al.vb.envois;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALParametresListViewBean extends BJadePersistentObjectListViewBean {

    private EnvoiParametresSimpleModelSearch searchModel = null;

    /**
	 * 
	 */
    public ALParametresListViewBean() {
        super();
        searchModel = new EnvoiParametresSimpleModelSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new ALParametresViewBean(
                (EnvoiParametresSimpleModel) searchModel.getSearchResults()[idx]) : new ALParametresViewBean();
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
    public EnvoiParametresSimpleModelSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(EnvoiParametresSimpleModelSearch searchModel) {
        this.searchModel = searchModel;
    }

}
