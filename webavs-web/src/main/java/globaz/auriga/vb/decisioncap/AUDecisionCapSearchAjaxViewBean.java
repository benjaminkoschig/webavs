package globaz.auriga.vb.decisioncap;

import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;

public class AUDecisionCapSearchAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean afficherDecisionsSupprimmes = false;
    private transient DecisionCAPSearchModel searchModel = null;

    public AUDecisionCapSearchAjaxViewBean() {
        super();
        initList();
    }

    @Override
    public void find() throws Exception {

        if (!JadeStringUtil.isBlank(searchModel.getForIdAffiliation())) {
            // recherche via le service

            if (!afficherDecisionsSupprimmes) {
                searchModel.setForNotEtat(AUDecisionEtat.SUPPRIMEE.getCodeSystem());
            }

            searchModel = AurigaServiceLocator.getDecisionCAPService().search(searchModel);
        }
    }

    public List<AUDecisionCapSearchLineViewBean> getLineViewBeans() throws Exception {
        List<AUDecisionCapSearchLineViewBean> list = new ArrayList<AUDecisionCapSearchLineViewBean>();
        if (searchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                SimpleDecisionCAP decisionCap = (SimpleDecisionCAP) abstractModel;
                list.add(new AUDecisionCapSearchLineViewBean(decisionCap));
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
        searchModel = new DecisionCAPSearchModel();
    }

    public void setAfficherDecisionsSupprimmes(boolean afficherDecisionsSupprimmes) {
        this.afficherDecisionsSupprimmes = afficherDecisionsSupprimmes;
    }

    public void setSearchModel(DecisionCAPSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
