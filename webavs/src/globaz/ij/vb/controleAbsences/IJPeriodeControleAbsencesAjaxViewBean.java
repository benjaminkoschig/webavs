package globaz.ij.vb.controleAbsences;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.ij.business.models.IJPeriodeControleAbsences;
import ch.globaz.ij.business.models.IJPeriodeControleAbsencesSearchModel;
import ch.globaz.ij.business.services.IJServiceLocator;

public class IJPeriodeControleAbsencesAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<IJPeriodeControleAbsences, IJPeriodeControleAbsencesSearchModel> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IJPeriodeControleAbsences periode;
    private IJPeriodeControleAbsencesSearchModel searchModel;

    public IJPeriodeControleAbsencesAjaxViewBean() {
        super();
        periode = new IJPeriodeControleAbsences();
        searchModel = new IJPeriodeControleAbsencesSearchModel();
    }

    @Override
    public IJPeriodeControleAbsences getCurrentEntity() {
        return periode;
    }

    public final IJPeriodeControleAbsences getPeriode() {
        return periode;
    }

    @Override
    public IJPeriodeControleAbsencesSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<IJPeriodeControleAbsences, IJPeriodeControleAbsencesSearchModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return IJServiceLocator.getControlePeriodeService();
    }

    @Override
    public void initList() {
        searchModel = new IJPeriodeControleAbsencesSearchModel();
    }

    @Override
    public void setCurrentEntity(IJPeriodeControleAbsences entite) {
        periode = entite;
    }

    @Override
    public void setSearchModel(IJPeriodeControleAbsencesSearchModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
