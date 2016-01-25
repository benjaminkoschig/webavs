/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleChampSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
public class AMChampListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleChampSearch champListSearch = null;
    private SignetListModelSearch signetListModelSearch = null;

    /**
	  *
	  */
    public AMChampListViewBean() {
        super();
        // this.champListSearch = new SimpleChampSearch();
        signetListModelSearch = new SignetListModelSearch();
    }

    @Override
    public void find() throws Exception {
        // this.champListSearch = ENServiceLocator.getSimpleChampService().search(this.champListSearch);
        signetListModelSearch = ENServiceLocator.getSignetListModelService().search(signetListModelSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        /*
         * return (idx < this.champListSearch.getSize() ? new AMChampViewBean( (SimpleChamp)
         * this.champListSearch.getSearchResults()[idx]) : new AMChampViewBean());
         */
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */

    /**
     * @return the dossierSearch
     */
    public SimpleChampSearch getFormuleListSearch() {
        return champListSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return champListSearch;
    }

    /**
     * @param dossierSearch
     *            the dossierSearch to set
     */
    public void setFormuleListSearch(SimpleChampSearch champformuleListSearch) {
        champListSearch = champformuleListSearch;
    }

}
