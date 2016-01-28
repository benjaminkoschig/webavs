package globaz.perseus.vb.qd;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.globall.db.BSession;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PFValidationFactureAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    private FactureSearchModel searchModel;

    public PFValidationFactureAjaxViewBean() {
        initList();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getFactureService().search(searchModel);
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void initList() {
        searchModel = new FactureSearchModel();
    }

    public String displayBeneficiaire(PersonneEtendueComplexModel personneEtendueComplexModel) {
        return PFUserHelper.getDetailAssure((BSession) getISession(), personneEtendueComplexModel);
    }
}
