/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMFormuleListViewBean extends BJadePersistentObjectListViewBean {
    private ParametreModelComplexSearch parametreModelComplexSearch = null;

    /**
	  *
	  */
    public AMFormuleListViewBean() {
        super();
        parametreModelComplexSearch = new ParametreModelComplexSearch();
    }

    @Override
    public void find() throws Exception {
        // this.formuleListSearch = AmalServiceLocator.getFormuleListService().search(this.formuleListSearch);
        parametreModelComplexSearch = AmalServiceLocator.getParametreModelService().search(parametreModelComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < parametreModelComplexSearch.getSize() ? new AMFormuleViewBean(
                (ParametreModelComplex) parametreModelComplexSearch.getSearchResults()[idx]) : new AMFormuleViewBean());
        // return idx < this.contribuableSearch.getSize() ? new AMContribuableViewBean(
        // (Contribuable) this.contribuableSearch.getSearchResults()[idx])
        // : new AMContribuableViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return parametreModelComplexSearch;
    }

    public ParametreModelComplexSearch getParametreModelComplexSearch() {
        return parametreModelComplexSearch;
    }

    public void setParametreModelComplexSearch(ParametreModelComplexSearch parametreModelComplexSearch) {
        this.parametreModelComplexSearch = parametreModelComplexSearch;
    }

}
