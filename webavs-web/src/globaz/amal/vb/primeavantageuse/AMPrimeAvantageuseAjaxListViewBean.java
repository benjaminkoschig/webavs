package globaz.amal.vb.primeavantageuse;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMPrimeAvantageuseAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimplePrimeAvantageuseSearch simplePrimeAvantageuseSearch = null;

    public AMPrimeAvantageuseAjaxListViewBean() {
        super();
        simplePrimeAvantageuseSearch = new SimplePrimeAvantageuseSearch();
    }

    @Override
    public void find() throws Exception {
        simplePrimeAvantageuseSearch = AmalServiceLocator.getSimplePrimeAvantageuseService().search(
                simplePrimeAvantageuseSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simplePrimeAvantageuseSearch.getSize() ? new AMPrimeAvantageuseViewBean(
                (SimplePrimeAvantageuse) simplePrimeAvantageuseSearch.getSearchResults()[idx])
                : new AMPrimeAvantageuseViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simplePrimeAvantageuseSearch;
    }

    public SimplePrimeAvantageuseSearch getSimplePrimeAvantageuseSearch() {
        return simplePrimeAvantageuseSearch;
    }

    public void setSimplePrimeAvantageuseSearch(SimplePrimeAvantageuseSearch simplePrimeAvantageuseSearch) {
        this.simplePrimeAvantageuseSearch = simplePrimeAvantageuseSearch;
    }

}
