/**
 * 
 */
package globaz.amal.vb.parametremodel;

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
public class AMParametreModelListViewBean extends BJadePersistentObjectListViewBean {
    private ParametreModelComplexSearch parametreModelComplexSearch = null;

    /**
	  *
	  */
    public AMParametreModelListViewBean() {
        super();
        parametreModelComplexSearch = new ParametreModelComplexSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        parametreModelComplexSearch = AmalServiceLocator.getParametreModelService().search(parametreModelComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < parametreModelComplexSearch.getSize() ? new AMParametreModelViewBean(
                (ParametreModelComplex) parametreModelComplexSearch.getSearchResults()[idx])
                : new AMParametreModelViewBean());
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
