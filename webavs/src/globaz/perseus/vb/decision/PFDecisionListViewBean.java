package globaz.perseus.vb.decision;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * 
 * @author MBO
 * 
 */
public class PFDecisionListViewBean extends BJadePersistentObjectListViewBean {

    private DecisionSearchModel searchModel;

    public PFDecisionListViewBean() {
        super();
        searchModel = new DecisionSearchModel();
    }

    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getDecisionService().search(searchModel);
        getISession().setAttribute("likeNss", searchModel.getLikeNss());
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PFDecisionViewBean((Decision) searchModel.getSearchResults()[idx])
                : new PFDecisionViewBean();
    }

    public String getCurrentUserFullName() {
        return getSession().getUserFullName();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;

    }

    public DecisionSearchModel getSearchModel() {
        return searchModel;
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    public void setSearchModel(DecisionSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
