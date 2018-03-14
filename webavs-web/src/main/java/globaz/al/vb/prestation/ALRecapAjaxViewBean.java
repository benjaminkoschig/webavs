package globaz.al.vb.prestation;

import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexModel;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class ALRecapAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private EntetePrestationListRecapComplexSearchModel recapSearchModel = new EntetePrestationListRecapComplexSearchModel();

    public ALRecapAjaxViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void find() throws Exception {
        if (!JadeStringUtil.isBlankOrZero(recapSearchModel.getForIdRecap())) {
            recapSearchModel = ALServiceLocator.getEntetePrestationListRecapComplexModelService().search(
                    recapSearchModel);
        }
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return new EntetePrestationListRecapComplexModel();
    }

    public List<EntetePrestationListRecapComplexModel> getList() {
        return PersistenceUtil.typeSearch(recapSearchModel, EntetePrestationListRecapComplexModel.class);
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return recapSearchModel;
    }

    @Override
    public void initList() {
        recapSearchModel = new EntetePrestationListRecapComplexSearchModel();
    }

    @Override
    public void retrieve() throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void update() throws Exception {
        throw new NotImplementedException();
    }
}
