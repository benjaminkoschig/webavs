package globaz.perseus.vb.dossier;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFDossierListViewBean extends BJadePersistentObjectListViewBean {
    private DossierSearchModel searchModel = null;

    public PFDossierListViewBean() throws Exception {
        super();
        searchModel = new DossierSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getDossierService().search(searchModel);
        getISession().setAttribute("likeNss", searchModel.getLikeNss());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFDossierViewBean((Dossier) searchModel.getSearchResults()[idx])
                : new PFDossierViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public DossierSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(DossierSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
