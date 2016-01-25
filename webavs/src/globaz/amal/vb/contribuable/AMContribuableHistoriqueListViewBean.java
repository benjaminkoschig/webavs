/**
 * 
 */
package globaz.amal.vb.contribuable;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMContribuableHistoriqueListViewBean extends BJadePersistentObjectListViewBean {
    private ContribuableHistoriqueRCListeSearch contribuableHistoriqueRCListeSearch = null;

    public AMContribuableHistoriqueListViewBean() {
        super();
        contribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
    }

    @Override
    public void find() throws Exception {
        contribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService().searchHistoriqueRCListe(
                contribuableHistoriqueRCListeSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < contribuableHistoriqueRCListeSearch.getSize() ? new AMContribuableHistoriqueViewBean(
                (ContribuableHistoriqueRCListe) contribuableHistoriqueRCListeSearch.getSearchResults()[idx])
                : new AMContribuableHistoriqueViewBean();
    }

    public ContribuableHistoriqueRCListeSearch getContribuableHistoriqueRCListeSearch() {
        return contribuableHistoriqueRCListeSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return contribuableHistoriqueRCListeSearch;
    }

    @Override
    public int getSize() {
        return contribuableHistoriqueRCListeSearch.getSize();
    }

    public void setContribuableHistoriqueRCListeSearch(
            ContribuableHistoriqueRCListeSearch contribuableHistoriqueRCListeSearch) {
        this.contribuableHistoriqueRCListeSearch = contribuableHistoriqueRCListeSearch;
    }
}
