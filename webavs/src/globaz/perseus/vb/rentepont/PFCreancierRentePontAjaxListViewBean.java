package globaz.perseus.vb.rentepont;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFCreancierRentePontAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CreancierRentePontSearchModel creancierRentePontSearch;

    public PFCreancierRentePontAjaxListViewBean() {
        super();
        creancierRentePontSearch = new CreancierRentePontSearchModel();
    }

    @Override
    public void find() throws Exception {
        creancierRentePontSearch = PerseusServiceLocator.getCreancierRentePontService()
                .search(creancierRentePontSearch);
    }

    public CreancierRentePontSearchModel getCreancierRentePontSearchModel() {
        return creancierRentePontSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return creancierRentePontSearch;
    }

    public void setCreancierRentePontSearchModel(CreancierRentePontSearchModel creancierRentePontSearch) {
        this.creancierRentePontSearch = creancierRentePontSearch;
    }

}
