package globaz.perseus.vb.qd;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * Viewbean permettant de gérer l'affichage d'une liste de factures.
 * 
 * @author JSI
 * 
 */
public class PFFactureListViewBean extends BJadePersistentObjectListViewBean {

    private FactureSearchModel searchModel = null;

    public PFFactureListViewBean() {
        super();
        searchModel = new FactureSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getFactureService().search(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFFactureViewBean((Facture) searchModel.getSearchResults()[idx])
                : new PFFactureViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public FactureSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(FactureSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
