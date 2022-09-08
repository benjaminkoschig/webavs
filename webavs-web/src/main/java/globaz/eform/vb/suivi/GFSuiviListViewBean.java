package globaz.eform.vb.suivi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class GFSuiviListViewBean  extends BJadePersistentObjectListViewBean {

    private GFDaDossierSearch suiviSearch;

    public GFSuiviListViewBean() {
        super();
        suiviSearch = new GFDaDossierSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return suiviSearch;
    }

    @Override
    public void find() throws Exception {
        suiviSearch.setWhereKey("suivi");
        suiviSearch = GFEFormServiceLocator.getGFDaDossierDBService().search(suiviSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < suiviSearch.getSize() ? new GFSuiviViewBean(
                (GFDaDossierModel) suiviSearch.getSearchResults()[idx]) : new GFSuiviViewBean();
    }
}
