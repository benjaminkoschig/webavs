package globaz.eform.vb.suivi;

import ch.globaz.eform.business.search.GFSuiviSearch;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class GFSuiviListViewBean  extends BJadePersistentObjectListViewBean {

    private GFSuiviSearch suiviSearch;

    public GFSuiviListViewBean() {
        super();
        suiviSearch = new GFSuiviSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return suiviSearch;
    }

    @Override
    public void find() throws Exception {

    }
}
