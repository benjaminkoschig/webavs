package globaz.perseus.vb.creancier;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DMA
 * 
 */
public class PFCreancierAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private CreancierSearchModel creancierSearch;

    public PFCreancierAjaxListViewBean() {
        super();
        creancierSearch = new CreancierSearchModel();
    }

    @Override
    public void find() throws Exception {
        creancierSearch = PerseusServiceLocator.getCreancierService().search(creancierSearch);
    }

    public CreancierSearchModel getCreancierSearchModel() {
        return creancierSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return creancierSearch;
    }

    public void setCreancierSearchModel(CreancierSearchModel creancierSearch) {
        this.creancierSearch = creancierSearch;
    }

}
