package globaz.amal.vb.annoncesassurance;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisse;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMAnnoncesAssuranceAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private AnnoncesCaisseSearch annoncesCaisseSearch = null;

    public AMAnnoncesAssuranceAjaxListViewBean() {
        super();
        annoncesCaisseSearch = new AnnoncesCaisseSearch();
    }

    @Override
    public void find() throws Exception {
        annoncesCaisseSearch = AmalServiceLocator.getSimpleAnnonceService().search(annoncesCaisseSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < annoncesCaisseSearch.getSize() ? new AMAnnoncesAssuranceAjaxViewBean(
                (AnnoncesCaisse) annoncesCaisseSearch.getSearchResults()[idx]) : new AMAnnoncesAssuranceAjaxViewBean();
    }

    public AnnoncesCaisseSearch getAnnoncesCaisseSearch() {
        return annoncesCaisseSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return annoncesCaisseSearch;
    }

    public AnnoncesCaisseSearch getSimpleAnnonceSearch() {
        return annoncesCaisseSearch;
    }

    public void setAnnoncesCaisseSearch(AnnoncesCaisseSearch annoncesCaisseSearch) {
        this.annoncesCaisseSearch = annoncesCaisseSearch;
    }

    public void setSimpleAnnonceSearch(AnnoncesCaisseSearch annoncesCaisseSearch) {
        this.annoncesCaisseSearch = annoncesCaisseSearch;
    }

}
