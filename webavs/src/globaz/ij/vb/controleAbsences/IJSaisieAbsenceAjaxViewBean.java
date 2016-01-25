package globaz.ij.vb.controleAbsences;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.business.services.IJServiceLocator;

public class IJSaisieAbsenceAjaxViewBean extends JadeAbstractAjaxCrudFindViewBean<IJAbsence, IJAbsenceSearchModel> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IJAbsence absence;
    private IJAbsenceSearchModel searchModel;

    public IJSaisieAbsenceAjaxViewBean() {
        super();
        absence = new IJAbsence();
        searchModel = new IJAbsenceSearchModel();
    }

    public IJAbsence getAbsence() {
        return absence;
    }

    @Override
    public IJAbsence getCurrentEntity() {
        return absence;
    }

    @Override
    public IJAbsenceSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<IJAbsence, IJAbsenceSearchModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return IJServiceLocator.getAbsenceService();
    }

    @Override
    public void initList() {
        searchModel = new IJAbsenceSearchModel();

    }

    @Override
    public void setCurrentEntity(IJAbsence entite) {
        absence = entite;
    }

    @Override
    public void setSearchModel(IJAbsenceSearchModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }

}
