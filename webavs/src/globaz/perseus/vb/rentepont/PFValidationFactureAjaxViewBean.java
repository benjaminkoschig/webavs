package globaz.perseus.vb.rentepont;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.rentepont.FactureRentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFValidationFactureAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    private FactureRentePontSearchModel searchModel;

    public PFValidationFactureAjaxViewBean() {
        initList();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getFactureRentePontService().search(searchModel);
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void initList() {
        searchModel = new FactureRentePontSearchModel();
    }

}
