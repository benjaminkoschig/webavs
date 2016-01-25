package globaz.aries.vb.sortiecgas;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliationSearchModel;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.aries.exceptions.AriesNotImplementedException;

public class ARSortieCgasAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas;
    private String etatSortieLibelle;
    private transient ComplexSortieCGASDecisionCGASAffiliationSearchModel searchModel;
    private String typeAffiliationLibelle;

    public ARSortieCgasAjaxViewBean() {
        super();
        initList();
        complexSortieCgas = new ComplexSortieCGASDecisionCGASAffiliation();
    }

    @Override
    public void add() throws Exception {
        throw new AriesNotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new AriesNotImplementedException();
    }

    @Override
    public void find() throws Exception {
        searchModel = AriesServiceLocator.getSortieCGASService().search(searchModel);
    }

    public ComplexSortieCGASDecisionCGASAffiliation getComplexSortieCgas() {
        return complexSortieCgas;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return complexSortieCgas;
    }

    public List<ARSortieCgasLineViewBean> getLineViewBeans() throws Exception {
        List<ARSortieCgasLineViewBean> list = new ArrayList<ARSortieCgasLineViewBean>();
        if (searchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas = (ComplexSortieCGASDecisionCGASAffiliation) abstractModel;
                list.add(new ARSortieCgasLineViewBean(complexSortieCgas));
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
        searchModel = new ComplexSortieCGASDecisionCGASAffiliationSearchModel();
    }

    @Override
    public void retrieve() throws Exception {
        complexSortieCgas = AriesServiceLocator.getSortieCGASService().readComplexSortie(complexSortieCgas.getId());
        etatSortieLibelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                complexSortieCgas.getSortieCgas().getEtat());
        typeAffiliationLibelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                complexSortieCgas.getAffiliation().getTypeAffiliation());
    }

    public void setComplexSortieCgas(ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas) {
        this.complexSortieCgas = complexSortieCgas;
    }

    public void setSearchModel(ComplexSortieCGASDecisionCGASAffiliationSearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void update() throws Exception {
        throw new AriesNotImplementedException();
    }
}
