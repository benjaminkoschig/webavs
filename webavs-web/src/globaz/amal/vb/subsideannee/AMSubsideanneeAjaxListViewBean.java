package globaz.amal.vb.subsideannee;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMSubsideanneeAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = null;

    public AMSubsideanneeAjaxListViewBean() {
        super();
        simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
    }

    @Override
    public void find() throws Exception {
        simpleSubsideAnneeSearch = AmalServiceLocator.getSimpleSubsideAnneeService().search(simpleSubsideAnneeSearch);

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleSubsideAnneeSearch.getSize() ? new AMSubsideanneeAjaxViewBean(
                (SimpleSubsideAnnee) simpleSubsideAnneeSearch.getSearchResults()[idx])
                : new AMSubsideanneeAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleSubsideAnneeSearch;
    }

    public SimpleSubsideAnneeSearch getSimpleSubsideAnneeSearch() {
        return simpleSubsideAnneeSearch;
    }

    public void setSimpleSubsideAnneeSearch(SimpleSubsideAnneeSearch simpleSubsideAnneeSearch) {
        this.simpleSubsideAnneeSearch = simpleSubsideAnneeSearch;
    }

}
