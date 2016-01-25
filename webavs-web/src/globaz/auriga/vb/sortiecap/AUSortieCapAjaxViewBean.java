package globaz.auriga.vb.sortiecap;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliationSearchModel;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.auriga.exceptions.AurigaNotImplementedException;

public class AUSortieCapAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ComplexSortieCAPDecisionCAPAffiliation complexSortieCap;
    private String etatSortieLibelle;
    private transient ComplexSortieCAPDecisionCAPAffiliationSearchModel searchModel;
    private String typeAffiliationLibelle;

    public AUSortieCapAjaxViewBean() {
        super();
        initList();
        complexSortieCap = new ComplexSortieCAPDecisionCAPAffiliation();
    }

    @Override
    public void add() throws Exception {
        throw new AurigaNotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new AurigaNotImplementedException();
    }

    @Override
    public void find() throws Exception {
        searchModel = AurigaServiceLocator.getSortieCAPService().search(searchModel);
    }

    public ComplexSortieCAPDecisionCAPAffiliation getComplexSortieCap() {
        return complexSortieCap;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return complexSortieCap;
    }

    public List<AUSortieCapLineViewBean> getLineViewBeans() throws Exception {
        List<AUSortieCapLineViewBean> list = new ArrayList<AUSortieCapLineViewBean>();
        if (searchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                ComplexSortieCAPDecisionCAPAffiliation complexSortieCap = (ComplexSortieCAPDecisionCAPAffiliation) abstractModel;
                list.add(new AUSortieCapLineViewBean(complexSortieCap));
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
        searchModel = new ComplexSortieCAPDecisionCAPAffiliationSearchModel();
    }

    @Override
    public void retrieve() throws Exception {
        complexSortieCap = AurigaServiceLocator.getSortieCAPService().readComplexSortie(complexSortieCap.getId());
        etatSortieLibelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                complexSortieCap.getSortieCap().getEtat());
        typeAffiliationLibelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                complexSortieCap.getAffiliation().getTypeAffiliation());
    }

    public void setComplexSortieCap(ComplexSortieCAPDecisionCAPAffiliation complexSortieCap) {
        this.complexSortieCap = complexSortieCap;
    }

    public void setSearchModel(ComplexSortieCAPDecisionCAPAffiliationSearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void update() throws Exception {
        throw new AurigaNotImplementedException();
    }
}
