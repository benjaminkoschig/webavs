package globaz.aries.vb.decisioncgas;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.models.DecisionCGASSearchModel;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.services.AriesServiceLocator;

public class ARDecisionCgasSearchAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean afficherDecisionsSupprimmes = false;
    private transient DecisionCGASSearchModel searchModel = null;

    public ARDecisionCgasSearchAjaxViewBean() {
        super();
        initList();
    }

    @Override
    public void find() throws Exception {

        if (!JadeStringUtil.isBlankOrZero(searchModel.getForIdAffiliation())) {

            if (!afficherDecisionsSupprimmes) {
                searchModel.setForNotEtat(ARDecisionEtat.SUPPRIMEE.getCodeSystem());
            }

            searchModel = AriesServiceLocator.getDecisionCGASService().search(searchModel);
        }

    }

    public List<ARDecisionCgasSearchLineViewBean> getLineViewBeans() throws Exception {
        List<ARDecisionCgasSearchLineViewBean> list = new ArrayList<ARDecisionCgasSearchLineViewBean>();
        if (searchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                SimpleDecisionCGAS decisionCgas = (SimpleDecisionCGAS) abstractModel;
                list.add(new ARDecisionCgasSearchLineViewBean(decisionCgas));
            }
        }
        return list;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void initList() {
        searchModel = new DecisionCGASSearchModel();
    }

    public void setAfficherDecisionsSupprimmes(boolean afficherDecisionsSupprimmes) {
        this.afficherDecisionsSupprimmes = afficherDecisionsSupprimmes;
    }

    public void setSearchModel(DecisionCGASSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
