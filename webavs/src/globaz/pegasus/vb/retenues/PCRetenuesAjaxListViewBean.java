package globaz.pegasus.vb.retenues;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRetenuesAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private PcaRetenueSearch search = new PcaRetenueSearch();

    public PCRetenuesAjaxListViewBean() {
        super();
    }

    @Override
    public void find() throws Exception {
        search = PegasusServiceLocator.getRetenueService().search(search);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < search.getSize() ? new PCRetenuesAjaxViewBean(
                ((PcaRetenue) search.getSearchResults()[idx]).getSimpleRetenue()) : new PCRetenuesAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return search;
    }

    public PcaRetenueSearch getPcaRetenueSearch() {
        return search;
    }

}
