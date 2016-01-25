package globaz.amal.vb.parametremodel;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMParametreModelAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private ParametreModelComplexSearch parametreModelComplexSearch = null;

    public AMParametreModelAjaxListViewBean() {
        super();
        parametreModelComplexSearch = new ParametreModelComplexSearch();
    }

    @Override
    public void find() throws Exception {
        parametreModelComplexSearch = AmalServiceLocator.getParametreModelService().search(parametreModelComplexSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < parametreModelComplexSearch.getSize() ? new AMParametreModelAjaxViewBean(
                (ParametreModelComplex) parametreModelComplexSearch.getSearchResults()[idx])
                : new AMParametreModelAjaxViewBean();
    }

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
