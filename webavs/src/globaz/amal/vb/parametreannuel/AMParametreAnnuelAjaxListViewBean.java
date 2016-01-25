package globaz.amal.vb.parametreannuel;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMParametreAnnuelAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = null;

    public AMParametreAnnuelAjaxListViewBean() {
        super();
        simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
    }

    @Override
    public void find() throws Exception {
        simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                .search(simpleParametreAnnuelSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleParametreAnnuelSearch.getSize() ? new AMParametreAnnuelViewBean(
                (SimpleParametreAnnuel) simpleParametreAnnuelSearch.getSearchResults()[idx])
                : new AMParametreAnnuelViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleParametreAnnuelSearch;
    }

    public SimpleParametreAnnuelSearch getSimpleParametreAnnuelSearch() {
        return simpleParametreAnnuelSearch;
    }

    public void setSimpleParametreAnnuelSearch(SimpleParametreAnnuelSearch simpleParametreAnnuelSearch) {
        this.simpleParametreAnnuelSearch = simpleParametreAnnuelSearch;
    }

}
