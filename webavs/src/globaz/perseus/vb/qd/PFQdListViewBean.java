/**
 * 
 */
package globaz.perseus.vb.qd;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFQdListViewBean extends BJadePersistentObjectListViewBean {

    private QDSearchModel searchModel = null;

    /**
	 * 
	 */
    public PFQdListViewBean() {
        super();
        searchModel = new QDSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        if (PerseusServiceLocator.getQDService().count(searchModel) == 1) {
            searchModel.getInCSTypeQD().add(searchModel.getForCSTypeQD());
            searchModel.setForCSTypeQD(null);
            searchModel.getInCSTypeQD().add(CSTypeQD.FRAIS_GARDE.getCodeSystem());
            searchModel.getInCSTypeQD().add(CSTypeQD.FRAIS_MALADIE.getCodeSystem());
        }
        searchModel = PerseusServiceLocator.getQDService().search(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFQdViewBean((QD) searchModel.getSearchResults()[idx])
                : new PFQdViewBean();
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
    public QDSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(QDSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
