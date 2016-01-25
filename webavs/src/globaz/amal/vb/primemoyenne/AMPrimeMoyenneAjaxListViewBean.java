package globaz.amal.vb.primemoyenne;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMPrimeMoyenneAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimplePrimeMoyenneSearch simplePrimeMoyenneSearch = null;

    public AMPrimeMoyenneAjaxListViewBean() {
        super();
        simplePrimeMoyenneSearch = new SimplePrimeMoyenneSearch();
    }

    @Override
    public void find() throws Exception {
        simplePrimeMoyenneSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(simplePrimeMoyenneSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simplePrimeMoyenneSearch.getSize() ? new AMPrimeMoyenneViewBean(
                (SimplePrimeMoyenne) simplePrimeMoyenneSearch.getSearchResults()[idx]) : new AMPrimeMoyenneViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simplePrimeMoyenneSearch;
    }

    public SimplePrimeMoyenneSearch getSimplePrimeMoyenneSearch() {
        return simplePrimeMoyenneSearch;
    }

    public void setSimplePrimeMoyenneSearch(SimplePrimeMoyenneSearch simplePrimeMoyenneSearch) {
        this.simplePrimeMoyenneSearch = simplePrimeMoyenneSearch;
    }

}
