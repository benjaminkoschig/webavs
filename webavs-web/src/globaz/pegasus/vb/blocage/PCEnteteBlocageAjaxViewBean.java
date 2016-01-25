package globaz.pegasus.vb.blocage;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.List;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.PcaBloqueSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class PCEnteteBlocageAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient PcaBloqueSearch search = new PcaBloqueSearch();

    @Override
    public void find() throws Exception {
        PegasusServiceLocator.getPcaBloqueService().searchPcaBloque(search);
    }

    public List<PcaBloque> getList() {
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return search;
    }

    @Override
    public void initList() {
        search = new PcaBloqueSearch();
    }
}
