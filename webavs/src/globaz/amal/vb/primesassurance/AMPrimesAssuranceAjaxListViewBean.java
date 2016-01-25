package globaz.amal.vb.primesassurance;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssurance;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssuranceSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMPrimesAssuranceAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimplePrimesAssuranceSearch simplePrimesAssuranceSearch = null;

    public AMPrimesAssuranceAjaxListViewBean() {
        super();
        simplePrimesAssuranceSearch = new SimplePrimesAssuranceSearch();
    }

    @Override
    public void find() throws Exception {
        simplePrimesAssuranceSearch = AmalServiceLocator.getPrimesAssuranceService()
                .search(simplePrimesAssuranceSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simplePrimesAssuranceSearch.getSize() ? new AMPrimesAssuranceAjaxViewBean(
                (SimplePrimesAssurance) simplePrimesAssuranceSearch.getSearchResults()[idx])
                : new AMPrimesAssuranceAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simplePrimesAssuranceSearch;
    }

    public SimplePrimesAssuranceSearch getSimplePrimesAssuranceSearch() {
        return simplePrimesAssuranceSearch;
    }

    public void setSimplePrimesAssuranceSearch(SimplePrimesAssuranceSearch simplePrimesAssuranceSearch) {
        this.simplePrimesAssuranceSearch = simplePrimesAssuranceSearch;
    }

}
