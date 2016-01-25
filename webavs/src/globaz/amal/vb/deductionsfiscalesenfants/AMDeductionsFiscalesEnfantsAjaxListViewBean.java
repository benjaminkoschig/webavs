package globaz.amal.vb.deductionsfiscalesenfants;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMDeductionsFiscalesEnfantsAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch = null;

    public AMDeductionsFiscalesEnfantsAjaxListViewBean() {
        super();
        simpleDeductionsFiscalesEnfantsSearch = new SimpleDeductionsFiscalesEnfantsSearch();
    }

    @Override
    public void find() throws Exception {
        simpleDeductionsFiscalesEnfantsSearch = AmalServiceLocator.getDeductionsFiscalesEnfantsService().search(
                simpleDeductionsFiscalesEnfantsSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleDeductionsFiscalesEnfantsSearch.getSize() ? new AMDeductionsFiscalesEnfantsViewBean(
                (SimpleDeductionsFiscalesEnfants) simpleDeductionsFiscalesEnfantsSearch.getSearchResults()[idx])
                : new AMDeductionsFiscalesEnfantsViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleDeductionsFiscalesEnfantsSearch;
    }

    /**
     * @return the simpleDeductionsFiscalesEnfantsSearch
     */
    public SimpleDeductionsFiscalesEnfantsSearch getSimpleDeductionsFiscalesEnfantsSearch() {
        return simpleDeductionsFiscalesEnfantsSearch;
    }

    /**
     * @param simpleDeductionsFiscalesEnfantsSearch
     *            the simpleDeductionsFiscalesEnfantsSearch to set
     */
    public void setSimpleDeductionsFiscalesEnfantsSearch(
            SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch) {
        this.simpleDeductionsFiscalesEnfantsSearch = simpleDeductionsFiscalesEnfantsSearch;
    }

}
