package globaz.perseus.vb.lot;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * 
 * @author MBO
 * 
 */

public class PFLotListViewBean extends BJadePersistentObjectListViewBean {

    private LotSearchModel lotSearch = new LotSearchModel();

    public PFLotListViewBean() {
        super();
        lotSearch = new LotSearchModel();
    }

    @Override
    public void find() throws Exception {
        lotSearch = PerseusServiceLocator.getLotService().search(lotSearch);

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < lotSearch.getSize() ? new PFLotViewBean((Lot) lotSearch.getSearchResults()[idx])
                : new PFLotViewBean();
    }

    public LotSearchModel getLotSearch() {
        return lotSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return lotSearch;

    }

    public void setLotSearch(LotSearchModel lotSearch) {
        this.lotSearch = lotSearch;
    }

}
