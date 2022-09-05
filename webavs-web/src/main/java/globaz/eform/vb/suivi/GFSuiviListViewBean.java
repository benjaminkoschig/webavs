package globaz.eform.vb.suivi;

import ch.globaz.eform.business.search.GFDaDossierSearch;
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

    }
}
