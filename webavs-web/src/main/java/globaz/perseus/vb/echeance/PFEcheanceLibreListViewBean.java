package globaz.perseus.vb.echeance;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.echeance.EcheanceLibre;
import ch.globaz.perseus.business.models.echeance.EcheanceLibreSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFEcheanceLibreListViewBean extends BJadePersistentObjectListViewBean {

    private EcheanceLibreSearchModel searchModel;

    public PFEcheanceLibreListViewBean() {
        super();
        searchModel = new EcheanceLibreSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getEcheanceLibreService().search(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFEcheanceLibreViewBean(
                (EcheanceLibre) searchModel.getSearchResults()[idx]) : new PFEcheanceLibreViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public EcheanceLibreSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(EcheanceLibreSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
