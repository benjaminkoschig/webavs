/**
 * 
 */
package globaz.perseus.vb.rentepont;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFRentePontListViewBean extends BJadePersistentObjectListViewBean {

    private RentePontSearchModel searchModel;

    /**
	 * 
	 */
    public PFRentePontListViewBean() throws Exception {
        super();
        searchModel = new RentePontSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getRentePontService().search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFRentePontViewBean((RentePont) searchModel.getSearchResults()[idx])
                : new PFRentePontViewBean();
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
    public RentePontSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(RentePontSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
