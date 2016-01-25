/**
 * 
 */
package globaz.amal.vb.contribuable;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMContribuableListViewBean extends BJadePersistentObjectListViewBean {
    private ContribuableRCListeSearch contribuableRCListeSearch = null;

    /**
	  *
	  */
    public AMContribuableListViewBean() {
        super();
        contribuableRCListeSearch = new ContribuableRCListeSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        if (contribuableRCListeSearch.getSearchInTiers() == null) {
            contribuableRCListeSearch.setWhereKey("notTiers");
        } else {
            if (contribuableRCListeSearch.getSearchInTiers() == false) {
                contribuableRCListeSearch.setWhereKey("notTiers");
            }
        }
        contribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                .searchRCListe(contribuableRCListeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < contribuableRCListeSearch.getSize() ? new AMContribuableViewBean(
                (ContribuableRCListe) contribuableRCListeSearch.getSearchResults()[idx]) : new AMContribuableViewBean();
    }

    public ContribuableRCListeSearch getContribuableRCListeSearch() {
        return contribuableRCListeSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return contribuableRCListeSearch;
    }

    @Override
    public int getSize() {
        return contribuableRCListeSearch.getSize();
    }

    public void setContribuableRCListeSearch(ContribuableRCListeSearch contribuableRCListeSearch) {
        this.contribuableRCListeSearch = contribuableRCListeSearch;
    }

}
