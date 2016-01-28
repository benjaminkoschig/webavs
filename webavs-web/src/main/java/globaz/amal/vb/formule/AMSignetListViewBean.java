/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleSignetSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
public class AMSignetListViewBean extends BJadePersistentObjectListViewBean {
    private String id = null;
    private SimpleSignetSearch simpleSignetSearch = null;

    /**
	  *
	  */
    public AMSignetListViewBean() {
        super();
        simpleSignetSearch = new SimpleSignetSearch();
    }

    @Override
    public void find() throws Exception {
        // this.formuleListSearch = AmalServiceLocator.getFormuleListService().search(this.formuleListSearch);
        simpleSignetSearch = ENServiceLocator.getSimpleSignetService().search(simpleSignetSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        /*
         * return (idx < this.simpleSignetSearch.getSize() ? new AMChampViewBean( (SimpleChamp)
         * this.champListSearch.getSearchResults()[idx]) : new AMChampViewBean());
         */
        return null;

    }

    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleSignetSearch;
    }

    public SimpleSignetSearch getSimpleSignetSearch() {
        return simpleSignetSearch;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSimpleSignetSearch(SimpleSignetSearch simpleSignetSearch) {
        this.simpleSignetSearch = simpleSignetSearch;
    }

}
