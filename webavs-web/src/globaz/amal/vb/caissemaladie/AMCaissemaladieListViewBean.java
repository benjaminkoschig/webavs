package globaz.amal.vb.caissemaladie;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMCaissemaladieListViewBean extends BJadePersistentObjectListViewBean {
    // CaisseMaladieGroupeRCListeSearch searchModel = null;
    CaisseMaladieSearch searchModel = null;

    public AMCaissemaladieListViewBean() {
        super();
        // this.searchModel = new CaisseMaladieGroupeRCListeSearch();
        searchModel = new CaisseMaladieSearch();
    }

    @Override
    public void find() throws Exception {
        if (!JadeStringUtil.isEmpty(searchModel.getLikeNomCaisse())) {
            searchModel.setLikeNomCaisse(searchModel.getLikeNomCaisse().toUpperCase());
        }

        searchModel = AmalServiceLocator.getCaisseMaladieService().search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        // return idx < this.searchModel.getSize() ? new AMCaissemaladieViewBean(
        // (CaisseMaladieGroupeRCListe) this.searchModel.getSearchResults()[idx]) : new AMCaissemaladieViewBean();
        return idx < searchModel.getSize() ? new AMCaissemaladieViewBean(
                (CaisseMaladie) searchModel.getSearchResults()[idx]) : new AMCaissemaladieViewBean();
    }

    public List<String> getIdTiersCMInProgress() {
        List<String> currentCMInProgress = new ArrayList<String>();
        try {
            currentCMInProgress = AmalServiceLocator.getAnnonceService().getIdTiersCMAnnonceInProgress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentCMInProgress;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    // public CaisseMaladieGroupeRCListeSearch getSearchModel() {
    // return this.searchModel;
    // }

    // public void setSearchModel(CaisseMaladieGroupeRCListeSearch searchModel) {
    // this.searchModel = searchModel;
    // }

    public CaisseMaladieSearch getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(CaisseMaladieSearch searchModel) {
        this.searchModel = searchModel;
    }
}
