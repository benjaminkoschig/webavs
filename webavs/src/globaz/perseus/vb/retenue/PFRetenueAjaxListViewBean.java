package globaz.perseus.vb.retenue;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFRetenueAjaxListViewBean extends BJadePersistentObjectListViewBean {
    private SimpleRetenueSearchModel simpleRetenueSearchModel = null;

    public PFRetenueAjaxListViewBean() {
        super();
        simpleRetenueSearchModel = new SimpleRetenueSearchModel();
    }

    @Override
    public void find() throws Exception {
        simpleRetenueSearchModel = PerseusImplServiceLocator.getSimpleRetenueService().search(simpleRetenueSearchModel);

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleRetenueSearchModel.getSize() ? new PFRetenueAjaxViewBean(
                (SimpleRetenue) simpleRetenueSearchModel.getSearchResults()[idx]) : new PFRetenueAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleRetenueSearchModel;
    }

    /**
     * @return the retenueSearchModel
     */
    public SimpleRetenueSearchModel getSimpleRetenueSearchModel() {
        return simpleRetenueSearchModel;
    }

    /**
     * @param retenueSearchModel
     *            the retenueSearchModel to set
     */
    public void setSimpleRetenueSearchModel(SimpleRetenueSearchModel simpleRetenueSearchModel) {
        this.simpleRetenueSearchModel = simpleRetenueSearchModel;
    }

}
