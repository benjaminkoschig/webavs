package globaz.perseus.vb.rentepont;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.FactureRentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author jsi
 * 
 */
public class PFFactureRentePontListViewBean extends BJadePersistentObjectListViewBean {

    private FactureRentePontSearchModel searchModel;

    public PFFactureRentePontListViewBean() {
        super();
        searchModel = new FactureRentePontSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getFactureRentePontService().search(searchModel);
        getISession().setAttribute("likeNss", searchModel.getLikeNss());
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFFactureRentePontViewBean(
                (FactureRentePont) searchModel.getSearchResults()[idx]) : new PFFactureRentePontViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public FactureRentePontSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(FactureRentePontSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
