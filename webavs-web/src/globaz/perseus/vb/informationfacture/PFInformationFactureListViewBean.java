package globaz.perseus.vb.informationfacture;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.models.informationfacture.InformationFactureSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFInformationFactureListViewBean extends BJadePersistentObjectListViewBean {

    private InformationFactureSearchModel searchModel;

    public PFInformationFactureListViewBean() {
        super();
        searchModel = new InformationFactureSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = PerseusServiceLocator.getInformationFactureService().search(searchModel);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFInformationFactureViewBean(
                (InformationFacture) searchModel.getSearchResults()[idx]) : new PFInformationFactureViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public InformationFactureSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(InformationFactureSearchModel searchModel) {
        this.searchModel = searchModel;
    }
}
