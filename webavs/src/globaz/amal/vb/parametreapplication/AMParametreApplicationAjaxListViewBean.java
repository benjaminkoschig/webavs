/**
 * 
 */
package globaz.amal.vb.parametreapplication;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMParametreApplicationAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleParametreApplicationSearch simpleParametreApplicationSearch = null;

    /**
     * Default constructor
     */
    public AMParametreApplicationAjaxListViewBean() {
        super();
        simpleParametreApplicationSearch = new SimpleParametreApplicationSearch();
    }

    @Override
    public void find() throws Exception {
        simpleParametreApplicationSearch = AmalServiceLocator.getSimpleParametreApplicationService().search(
                simpleParametreApplicationSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleParametreApplicationSearch.getSize() ? new AMParametreApplicationViewBean(
                (SimpleParametreApplication) simpleParametreApplicationSearch.getSearchResults()[idx])
                : new AMParametreApplicationViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleParametreApplicationSearch;
    }

    public SimpleParametreApplicationSearch getSimpleParametreApplicationSearch() {
        return simpleParametreApplicationSearch;
    }

    public void setSimpleParametreApplicationSearch(SimpleParametreApplicationSearch simpleParametreApplicationSearch) {
    }

}
